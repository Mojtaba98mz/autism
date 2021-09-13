package com.armaghanehayat.autism.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "en_name", nullable = false)
    private String enName;

    @OneToMany(mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "donations", "giverauditors", "absorbant", "supporter", "province", "city" }, allowSetters = true)
    private Set<Giver> givers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cities", "givers" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public City name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return this.enName;
    }

    public City enName(String enName) {
        this.enName = enName;
        return this;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Set<Giver> getGivers() {
        return this.givers;
    }

    public City givers(Set<Giver> givers) {
        this.setGivers(givers);
        return this;
    }

    public City addGiver(Giver giver) {
        this.givers.add(giver);
        giver.setCity(this);
        return this;
    }

    public City removeGiver(Giver giver) {
        this.givers.remove(giver);
        giver.setCity(null);
        return this;
    }

    public void setGivers(Set<Giver> givers) {
        if (this.givers != null) {
            this.givers.forEach(i -> i.setCity(null));
        }
        if (givers != null) {
            givers.forEach(i -> i.setCity(this));
        }
        this.givers = givers;
    }

    public Province getProvince() {
        return this.province;
    }

    public City province(Province province) {
        this.setProvince(province);
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return id != null && id.equals(((City) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enName='" + getEnName() + "'" +
            "}";
    }
}
