package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.asset.Asset;
import com.socnet.entity.asset.Attached;
import com.socnet.entity.enumaration.Role;
import com.socnet.validation.annotations.UniqueEmail;
import org.apache.catalina.authenticator.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.ToString(exclude = {"wall", "posts", "relations"})

@Entity
@UniqueEmail
public class User extends BaseEntity implements Attached {
    public interface UserView extends Asset.AssetView {
    }
    public interface UserExtendView extends Asset.AssetView{
    }

    @JsonView(UserView.class)
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @JsonView(UserView.class)
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @JsonView(UserView.class)
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 255)
    @Email
    @Column(unique = true, length = 255)
    private String email;

    @JsonView(UserExtendView.class)
    @NotNull
    @NotEmpty
    @Size(max = 36) //todo move 36 to Constants and name MD5Length
    @Column(length = 36)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wall wall;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts; //todo remove this fields from User model

    @JsonView(UserExtendView.class)
    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @JsonView(UserExtendView.class)
    @Temporal(TemporalType.DATE)
    @Column
    private Date birthday;

    @Column
    private boolean available;

    @Column
    private boolean online;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Relation> relations;

    @Transient
    private List<Asset> assets; //todo rename to avatars

    //todo prepersist and preupdate where email.toLowerCase()
}
