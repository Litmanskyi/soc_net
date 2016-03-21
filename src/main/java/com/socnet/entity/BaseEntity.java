package com.socnet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import java.util.Date;

@Setter
@Getter

@MappedSuperclass
public class BaseEntity {

    public interface BaseView {}

    @JsonView(BaseView.class)
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @JsonView(BaseView.class)
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @JsonView(BaseView.class)
    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @JsonView(BaseView.class)
    @Column(name = "delete_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;

    @Version
    @Column(name = "version")
    private int version;

    @PreUpdate
    public void setUpdateDate() {
        lastUpdate = new Date();
    }

    @PrePersist
    public void setCreateDate() {
        createDate = new Date();
    }

    @PreDestroy //todo check it
    public void setDeleteDate(){deleteDate = new Date();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
