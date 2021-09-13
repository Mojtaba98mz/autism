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
 * A Province.
 */
@Entity
@Table(name = "province")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Province implements Serializable {

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

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "givers", "province" }, allowSetters = true)
    private Set<City> cities = new HashSet<>();

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "donations", "giverauditors", "absorbant", "supporter", "province", "city" }, allowSetters = true)
    private Set<Giver> givers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Province id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Province name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return this.enName;
    }

    public Province enName(String enName) {
        this.enName = enName;
        return this;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Set<City> getCities() {
        return this.cities;
    }

    public Province cities(Set<City> cities) {
        this.setCities(cities);
        return this;
    }

    public Province addCity(City city) {
        this.cities.add(city);
        city.setProvince(this);
        return this;
    }

    public Province removeCity(City city) {
        this.cities.remove(city);
        city.setProvince(null);
        return this;
    }

    public void setCities(Set<City> cities) {
        if (this.cities != null) {
            this.cities.forEach(i -> i.setProvince(null));
        }
        if (cities != null) {
            cities.forEach(i -> i.setProvince(this));
        }
        this.cities = cities;
    }

    public Set<Giver> getGivers() {
        return this.givers;
    }

    public Province givers(Set<Giver> givers) {
        this.setGivers(givers);
        return this;
    }

    public Province addGiver(Giver giver) {
        this.givers.add(giver);
        giver.setProvince(this);
        return this;
    }

    public Province removeGiver(Giver giver) {
        this.givers.remove(giver);
        giver.setProvince(null);
        return this;
    }

    public void setGivers(Set<Giver> givers) {
        if (this.givers != null) {
            this.givers.forEach(i -> i.setProvince(null));
        }
        if (givers != null) {
            givers.forEach(i -> i.setProvince(this));
        }
        this.givers = givers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enName='" + getEnName() + "'" +
            "}";
    }
}
