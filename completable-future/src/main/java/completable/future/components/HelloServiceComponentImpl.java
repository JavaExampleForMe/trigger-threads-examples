package completable.future.components;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceComponentImpl implements HelloService {


//    public static void main(String[] args) {
//        Function<Integer, Integer> f1 = o -> {
//            System.out.println("------------ in f1  -------------");
//            if (o>3)
//                return o/0;
//            else
//                return o++;
//        };
//        final Function<Integer, Integer> f2 = f(f1);
//        f2.apply(3);
//        f2.apply(4);
//
//    }
//
//    private static Function<Integer, Integer> f (Function<Integer, Integer> f1){
//        final boolean[] failed = {false};
//        return number -> {
//           try{
//               System.out.println("------------ in f2  -------------");
//               if (failed[0])
//                   return null;
//               return f1.apply(number);
//           }
//           catch(Exception e) {
//               failed[0] = true;
//               return null;
//           }
//       };
//    }
//

     public void sayHello() {



        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("------------ Hello  -------------");
        System.out.println("-------------------------------------------------------------------------------------------");
    }


}
