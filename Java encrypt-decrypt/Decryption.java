import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decryption {

    private final StringBuilder initVector = new StringBuilder("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
    private int ROUNDS = 1;
    private StringBuilder KEY;

    public StringBuilder decryptCipherText(StringBuilder cipherText, StringBuilder _key, String mode) {
        //System.out.println("---------------------------");
        //System.out.println("DECRYPTION START");

        int lengthCipherText = cipherText.length();
        int numberOfBlocks = lengthCipherText / 96;
        int index = 0;
        StringBuilder plainText = new StringBuilder();
        String previousOneTimePad = initVector.toString();

        KEY = _key;

        //System.out.println("KEY: " + KEY);

        if (!mode.equals("OFB")) {
            // Shift left 11 times.
            for (int i = 0; i <= 10; i++) {
                KEY = leftShift(KEY);
            }
        }


        //System.out.println("KEY AFTER SHIFTS: " + KEY);
        //System.out.println("cipherText length = " + lengthCipherText);
        //System.out.println("There will be  = " + numberOfBlocks + " blocks");
        /*Making block-size of 96 and for each block call scramble function*/
        while (lengthCipherText != 0) {

            String resultBlock = "";
            String cipherTextGroup = cipherText.substring(index * 96, (index + 1) * 96);

            if (!mode.equals("OFB")) {
                //System.out.println("Block number = " + index);
                //System.out.println("Block number starting index = " + index * 96);
                //System.out.println("Block number ending index= " + (index + 1) * 96);

                if (mode.equals("CBC")) {
                    String previousCipherTextGroup = index == 0 ? initVector.toString() : cipherText.substring((index - 1) * 96, (index) * 96);

                    //System.out.println("CURRENT PARAMETERS: ");
                    //System.out.println("CIPHER TEXT: " + cipherTextGroup);
                    //System.out.println("PREVIOUS CIPHER TEXT: " + previousCipherTextGroup);

                    resultBlock = decryptBlock(cipherTextGroup, KEY);
                    resultBlock = xor(resultBlock, previousCipherTextGroup);
                } else if (mode.equals("ECB")) {
                    resultBlock = decryptBlock(cipherTextGroup, KEY);
                }

            } else {
                String oneTimePad = encryptBlock(previousOneTimePad, KEY);
                resultBlock = xor(cipherTextGroup, oneTimePad);
                previousOneTimePad = oneTimePad;
            }

            index++;
            lengthCipherText -= 96;
            plainText.append(resultBlock);
            ROUNDS = 1;
        }

        //System.out.println("DECRYPTION END");
        //System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");


        return plainText;
    }

    private String decryptBlock(String block, StringBuilder key) {
        //System.out.println("---------------------------");
        //System.out.println("DECRYPT BLOCK START");

        String leftBlock = block.substring(0, block.length() / 2);
        String rightBlock = block.substring(block.length() / 2);

        while (ROUNDS != 11) {

            String newRightBlock = leftBlock;

            //System.out.println("KEY: " + key);
            key = new StringBuilder(rightShift(key.toString()));
            //System.out.println("KEY AFTER RIGHT SHIFT: " + key);
            String permuteKey = permutateKey(key);
            //System.out.println("Key of the round = " + permuteKey);


            leftBlock = xor(rightBlock, scramble(leftBlock, new StringBuilder(permuteKey)));
            rightBlock = newRightBlock;

            ROUNDS++;
        }

        //System.out.println("DECRYPT BLOCK END");
        //System.out.println("---------------------------");

        //System.out.println("Plain block= " + leftBlock + rightBlock);
        return leftBlock + rightBlock;
    }

    private String encryptBlock(String block, StringBuilder key) {
        String leftBlock = block.substring(0, block.length() / 2);
        String rightBlock = block.substring(block.length() / 2);
        ROUNDS = 0;

        while (ROUNDS != 10) {
            String newLeftBlock = rightBlock;

            key = new StringBuilder(leftShift(key));

            //System.out.println("KEY: " + key);

            String permutatedKey = permutateKey(key);

            rightBlock = xor(leftBlock, scramble(rightBlock, new StringBuilder(permutatedKey)));
            leftBlock = newLeftBlock;

            ROUNDS++;
        }

        return leftBlock + rightBlock;
    }

    private String scramble(String leftBlock, StringBuilder permutatedKey) {

        //System.out.println("---------------------------");
        //System.out.println("SCRAMBLE START");

        SBoxSubs sBox = new SBoxSubs();
        List<String> scrambleSubResult = new ArrayList<>();

        // XOR with Key[i] and LeftBlock[i]
        for (int i = 0; i < 8; i++) {
            String keySubGroup = permutatedKey.substring(i * 6, (i + 1) * 6);
            String leftSubGroup = leftBlock.substring(i * 6, (i + 1) * 6);
            String xorResult = xor(keySubGroup, leftSubGroup);
            scrambleSubResult.add(xorResult);
        }

        // XOR with group[i] and group[i+1]
        for (int i = 0; i <= 6; i += 2) {
            String firstGroup = scrambleSubResult.get(i);
            String secondGroup = scrambleSubResult.get(i + 1);
            String xorResult = xor(firstGroup, secondGroup);
            scrambleSubResult.add(xorResult);
        }

        // Substitution
        for (int i = 0; i < scrambleSubResult.size(); i++) {
            scrambleSubResult.set(i, sBox.sBoxEnc(new StringBuilder(scrambleSubResult.get(i))).toString());
        }

        // Getting the result.
        StringBuilder result = new StringBuilder();
        for (String subgroup : scrambleSubResult) {
            result.append(subgroup);
        }

        //System.out.println("SCRAMBLE END");
        //System.out.println("---------------------------");

        return result.toString();
    }

    private String permutateKey(StringBuilder keyCopy) {

        //System.out.println("---------------------------");
        //System.out.println("PERMUTATE KEY START");
        //System.out.println("Round = " + ROUNDS);
        // First try:
        // Do a right shift first, then permutate
        char[] shiftedKey = keyCopy.toString().toCharArray();
        StringBuilder permutatedKey = new StringBuilder();

        /*Permuted choice is going to be decided whether the round number is even or odd*/
        for (int i = 0; i < shiftedKey.length; i++) {
            if (ROUNDS % 2 == i % 2) {
                permutatedKey.append(shiftedKey[i]);
            }
        }

        //System.out.println("KEY AFTER PERMUTATION: " + permutatedKey.toString());

        //System.out.println("PERMUTATE END");
        //System.out.println("---------------------------");

        return permutatedKey.toString();
    }

    private String xor(String left, String right) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < left.length(); i++) {
            result.append(left.charAt(i) ^ right.charAt(i));
        }
        return result.toString();
    }

    private String rightShift(String key) {
        //System.out.println("Key before right circular shift = " + key);
        char[] chars = key.toString().toCharArray();
        char last = chars[chars.length - 1];
        for (int i = chars.length - 1; i > 0; i--) {
            chars[i] = chars[i - 1];
        }
        chars[0] = last;
        ////System.out.println("Key after right circular shift = " + Arrays.toString(chars));
        return new String(chars);
    }

    /*Left circular shift result will be 96bits*/
    public StringBuilder leftShift(StringBuilder key) {
        char[] chars = key.toString().toCharArray();
        char first = chars[0];
        for (int i = 0; i < chars.length - 1; i++) {
            chars[i] = chars[i + 1];
        }
        chars[chars.length - 1] = first;
        return new StringBuilder(new String(chars));
    }
}