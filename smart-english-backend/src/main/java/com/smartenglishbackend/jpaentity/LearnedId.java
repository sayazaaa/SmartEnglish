package com.smartenglishbackend.jpaentity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class LearnedId implements Serializable {
    private String word;
    private Integer accountId;
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LearnedId other = (LearnedId) obj;
        return other.accountId.equals(accountId) && other.word.equals(word);
    }
    @Override
    public int hashCode() {
        return Objects.hash(word, accountId);
    }
}
