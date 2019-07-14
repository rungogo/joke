package com.lad.chestnut.util;

import com.lad.chestnut.pojo.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author lad
 * @date 2019/7/9
 */
public class UserUtils {
    public static User getCurrentHr() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
