package com.smartenglishbackend.jpaentity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class ModUseTimeId implements java.io.Serializable {
    private String modname;
    private Integer account_id;
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ModUseTimeId other = (ModUseTimeId) obj;
        return other.account_id.equals(account_id) && other.modname.equals(modname);
    }
    @Override
    public int hashCode() {
        return Objects.hash(modname, account_id);
    }
}
