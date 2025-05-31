package com.smartenglishbackend.jpaentity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class ModUseTimeId implements java.io.Serializable {
    private String modName;
    private Integer accountId;
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ModUseTimeId other = (ModUseTimeId) obj;
        return other.accountId.equals(accountId) && other.modName.equals(modName);
    }
    @Override
    public int hashCode() {
        return Objects.hash(modName, accountId);
    }
}
