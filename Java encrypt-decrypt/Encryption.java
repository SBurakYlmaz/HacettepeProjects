public class Encryption {
    private final StringBuilder initVector = new StringBuilder("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
    private int roundNumber = 10;

    public StringBuilder encryptPlainText(StringBuilder plainText, StringBuilder key, String mode) {
        int index = 0;
        StringBuilder cipherText = new StringBuilder();
        String firstOfbBlock = "";
        String firstCbcBlock;
        int lengthPlainText = plainText.length();
        //System.out.println("Plaintext length = " + plainText.length());
        //System.out.print("There will be  = " + plainText.length() / 96);
        //System.out.println(" block");
        /*Making block-size of 96 and for each block call scramble function*/
        while (lengthPlainText != 0) {
            String resultBlock;
            if (!mode.equals("OFB")) {
                //System.out.println("Block number = " + index);
                //System.out.println("Block number starting index = " + index * 96);
                //System.out.println("Block number ending index= " + (index + 1) * 96);
                if (mode.equals("CBC")) {
                    if (index == 0) {
                        firstCbcBlock = xorOperation(plainText.substring(0, 96), initVector);
                        resultBlock = scrambleFunction(firstCbcBlock, key);

                    } else {
                        // System.out.println(index);
                        //System.out.println(plainText.substring(index * 96, (index + 1) * 96));
                        resultBlock = xorOperation(plainText.substring(index * 96, (index + 1) * 96), new StringBuilder(cipherText.substring((index - 1) * 96, index * 96)));
                        resultBlock = scrambleFunction(resultBlock, key);
                    }

                } else {
                    resultBlock = scrambleFunction(plainText.substring(index * 96, (index + 1) * 96), key);

                }

            } else {
                if (index == 0)
                    firstOfbBlock = scrambleFunction(initVector.toString(), key);
                else
                    firstOfbBlock = scrambleFunction(firstOfbBlock, key);
                resultBlock = xorOperation(firstOfbBlock, new StringBuilder(plainText.substring(index * 96, (index + 1) * 96)));
            }
            index += 1;
            lengthPlainText -= 96;
            cipherText.append(resultBlock);
            roundNumber = 10;
        }
        return cipherText;
    }

    public String scrambleFunction(String block, StringBuilder key) {
        SBoxSubs sBox = new SBoxSubs();
        String leftBlock = block.substring(0, block.length() / 2);
        String rightBlock = block.substring(block.length() / 2);
        //System.out.println("left block is = " + leftBlock);
        //System.out.println("right block is = " + rightBlock);
        while (roundNumber != 0) {
            //System.out.println("Round number =" + roundNumber);
            /*result of this one is the left circular shift of a key*/
            char[] chars = keyFunctions(key);
            String newKey = String.valueOf(chars);
            //System.out.println("new key= "+ newKey);

            key = new StringBuilder(newKey);
            StringBuilder currentKey = new StringBuilder();

            /*Permuted choice is going to be decided whether the round number is even or odd*/
            if (roundNumber % 2 == 0) {
                for (int i = 0; i < chars.length; i += 2) {
                    currentKey.append(chars[i]);
                }
            } else {
                for (int i = 1; i < chars.length; i += 2) {
                    currentKey.append(chars[i]);
                }
            }
            //System.out.println("current 48 bit key = " + currentKey.toString());
            /*Key and right block xor*/
            //System.out.println("Right block = " + rightBlock);
            StringBuilder resultXor = new StringBuilder();
            for (int i = 0; i < rightBlock.length(); i++) {
                resultXor.append(rightBlock.charAt(i) ^ currentKey.charAt(i));
            }
            //System.out.println("48 bit key xor with right block = " + resultXor.toString());
            /*Complete to 72 bit*/
            int border = resultXor.length();
            for (int i = 0; i < border - 6; ) {
                resultXor.append(resultXor.charAt(i) ^ resultXor.charAt(i + 6));
                i++;
                if (i % 6 == 0)
                    i += 6;
            }
            //System.out.println("72 bit completed = "+ resultXor);

            /*Applying s-box operations*/
            String tempValue = leftBlock;
            leftBlock = rightBlock;
            rightBlock = xorOperation(tempValue, sBox.sBoxEnc(resultXor));
            roundNumber--;
            //System.out.println("New left block is = " + leftBlock);
            // System.out.println("New right block is = " + rightBlock);
        }
        //System.out.println("End of round for block size 96 and the cipher text for left block is = " + leftBlock);
        //System.out.println("End of round for block size 96 and the cipher text for right block is = " + rightBlock);
        return leftBlock + rightBlock;
    }

    public String xorOperation(String leftBlock, StringBuilder resultOfRightBlock) {
        // System.out.println("XOR operation is starting");
        //System.out.println("First input is = " + leftBlock);
        //System.out.println("Second input is = " + resultOfRightBlock.toString());
        StringBuilder rightBlock = new StringBuilder();
        for (int i = 0; i < leftBlock.length(); i++) {
            rightBlock.append(leftBlock.charAt(i) ^ resultOfRightBlock.charAt(i));
        }
        // System.out.println("Result of XOR operation is = " + rightBlock);
        return rightBlock.toString();
    }

    /*Left circular shift result will be 96bits*/
    public char[] keyFunctions(StringBuilder key) {
        //System.out.println("Key before left circular shift = " + key.toString());
        char[] chars = key.toString().toCharArray();
        char first = chars[0];
        for (int i = 0; i < chars.length - 1; i++) {
            chars[i] = chars[i + 1];
        }
        chars[chars.length - 1] = first;
        //System.out.println("Key after left circular shift = " + Arrays.toString(chars));
        return chars;
    }

}
