package epi.rangemining;

import java.util.List;
import java.util.Objects;

public class check_item {
    static boolean check_brock(String brock_type, List<?> check_bloc_list) {
        boolean check = false;
        for (int i = 0; i < Objects.requireNonNull(check_bloc_list).size(); i++) {
            //System.out.println(brock_type);
            //System.out.println(check_bloc_list.get(i));
            if (Objects.equals(brock_type, check_bloc_list.get(i))) {
                check = true;
                break;
            }
        }
        return check;
    }
}
