package com.lad.chestnut.constant;

import java.util.Optional;

/**
 * 用户controller
 *
 * @author lad
 * @date 2019/4/29
 */
public enum DeleteStatusEnum {
    /**
     * 删除
     */
    DELETE(0),

    /**
     * 未删除
     */
    NOT_DELETE(1);

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    DeleteStatusEnum(Integer status) {
        this.status = status;
    }

    public static Optional<DeleteStatusEnum> getDeleteStatusEnumByStatus(Integer status) {
        DeleteStatusEnum[] deleteStatusEnums = DeleteStatusEnum.values();
        for (DeleteStatusEnum deleteStatusEnum : deleteStatusEnums) {
            if (deleteStatusEnum.status.equals(status)) {
                return Optional.ofNullable(deleteStatusEnum);
            }
        }
        return Optional.empty();
    }

}
