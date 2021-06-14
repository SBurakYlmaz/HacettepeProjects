import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SBoxSubs {
    private final Map<String, String> sBoxSub = Stream.of(new String[][]{
            {"000000", "0010"},
            {"000010", "1100"},
            {"000100", "0100"},
            {"000110", "0001"},
            {"001000", "0111"},
            {"001010", "1010"},
            {"001100", "1011"},
            {"001110", "0110"},
            {"010000", "1000"},
            {"010010", "0101"},
            {"010100", "0011"},
            {"010110", "1111"},
            {"011000", "1101"},
            {"011010", "0000"},
            {"011100", "1110"},
            {"011110", "1001"},

            {"000001", "1110"},
            {"000011", "1011"},
            {"000101", "0010"},
            {"000111", "1100"},
            {"001001", "0100"},
            {"001011", "0111"},
            {"001101", "1101"},
            {"001111", "0001"},
            {"010001", "0101"},
            {"010011", "0000"},
            {"010101", "1111"},
            {"010111", "1010"},
            {"011001", "0011"},
            {"011011", "1001"},
            {"011101", "1000"},
            {"011111", "0110"},

            {"100000", "0100"},
            {"100010", "0010"},
            {"100100", "0001"},
            {"100110", "1011"},
            {"101000", "1010"},
            {"101010", "1101"},
            {"101100", "0111"},
            {"101110", "1000"},
            {"110000", "1111"},
            {"110010", "1001"},
            {"110100", "1100"},
            {"110110", "0101"},
            {"111000", "0110"},
            {"111010", "0011"},
            {"111100", "0000"},
            {"111110", "1110"},

            {"100001", "1011"},
            {"100011", "1000"},
            {"100101", "1100"},
            {"100111", "0111"},
            {"101001", "0001"},
            {"101011", "1110"},
            {"101101", "0010"},
            {"101111", "1101"},
            {"110001", "0110"},
            {"110011", "1111"},
            {"110101", "0000"},
            {"110111", "1001"},
            {"111001", "1010"},
            {"111011", "0100"},
            {"111101", "0101"},
            {"111111", "0011"},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public StringBuilder sBoxEnc(StringBuilder resultOfXor) {
        StringBuilder result = new StringBuilder();

        /*6 bits reduced to 4 bits considering the substitution rules 72 bits reduced 48 bits*/
        for (int i = 0; i < resultOfXor.length(); i += 6) {
            String s = resultOfXor.substring(i, i + 6);
            result.append(sBoxSub.get(s));
        }
        //System.out.println("Before permutation = " + result.toString());
        /*Permutation is applied to the resulting of 48 bits and result will be 48 bits */
        for (int i = 0; i < result.length(); i += 2) {
            char temp = result.charAt(i);
            result.setCharAt(i, result.charAt(i + 1));
            result.setCharAt(i + 1, temp);
        }
        // System.out.println("After permutation = "+ result.toString());
        return result;
    }
}
