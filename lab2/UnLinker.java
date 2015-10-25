/**
 * Created by linyx25 on 2015/10/5.
 */
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnLinker {

    /* @param text -> the string of a line in UnLinker.in
     * @param count -> to record the number after OMIT
     * RETURN -> the string of omit
     *
     * Attention:
     * 1. RegExp: (http://|www)?([a-zA-Z0-9\.]+)*(\.com|\.org|\.edu|\.info|\.tv){1}
     *      -> group(1) -> prefix (http:// | http://www | www)
     *      -> group(2) -> domain name (it can be "[a-zA-Z0-9]" ".")
     *      -> group(3) -> postfix (.com|.org|.edu|.info|.tv)
     *
     * 2. string.replaceFirst(String regex, String replacement)
     *      -> return Pattern.compile(regex).matcher(this).replaceFirst(replacement)
     *
     * ps:
     * 1. to replace the string from url to OMIT, do not use string.replace, because relace will replace all of the match str
     *    and we will get the result like that "check OMIT1 4 OMIT1 OMIT2"
     */
    String clean(String text) {
        int count = 1;
        Pattern pattern = Pattern.compile("(http://|www)?([a-zA-Z0-9\\.]+)*(\\.com|\\.org|\\.edu|\\.info|\\.tv){1}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = text.replaceFirst(matcher.group(0), "OMIT" + count);
            count += 1;
        }
        return text;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("UnLinker.in"));
        String text;
        UnLinker ul = new UnLinker();
        while (in.hasNextLine()) {
            text = in.nextLine();
            System.out.println(ul.clean(text));
        }
        in.close();
    }
}
