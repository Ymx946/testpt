package com.mz.common.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AddressVO {
    /**
     * 国
     */
    private String nation;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 国+省+市
     */
    private String address;

}
