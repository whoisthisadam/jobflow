package com.app.category;

import com.app.appUser.AppUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "category_g")
    @SequenceGenerator(name = "category_g", sequenceName = "category_seq", allocationSize = 1)
    private Long id;

    private String name;
    private float sum;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REFRESH)
    private List<AppUser> users = new ArrayList<>();

    public Category(Long id, String name, float sum) {
        this.id = id;
        this.name = name;
        this.sum = sum;
    }

    public Category(String name, float sum) {
        this.name = name;
        this.sum = sum;
    }

    public void update(Category update) {
        this.name = update.getName();
        this.sum = update.getSum();
    }
}