package com.example.mierul.myapplication21;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Hexa-Amierul.Japri on 20/4/2017.
 */

public class FragmentStack {
    private static Stack<Fragment> stack = new Stack<>();

    public static Fragment getPrevious(){
        //delete top stack
        stack.pop();
        //return previous fragment
        return stack.isEmpty()?null:stack.get(stack.size()-1);
    }

    public static void addStack(Fragment fragment) {
        //add fragment
        stack.push(fragment);
    }

    public static boolean isEmpty(){
        //only to be used by activity
        //which left only firstfragment
        if(stack.size()==1) {
            stack.pop();
        }
        return stack.isEmpty();
    }

    private static Fragment getFragment(int index){
        return !stack.isEmpty()&&index>=0 ? stack.get(index):null;
    }

    public static Fragment getFirstFragment(){
        return getFragment(0);
    }


}
