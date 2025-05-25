package com.finco.finco.infrastructure.config.db.schema;

import java.time.LocalDate;

import com.finco.finco.entity.asset.model.Asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "assets")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AssetSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserSchema user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "estimated_value",
            precision = 10, scale = 2,
            nullable = false)
    private Long estimatedValue;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @Column(name = "interest_rate", precision = 10, scale = 2)
    private Long interestRate;

    private String description;

    public Asset toAsset() {
        Asset asset = new Asset(
            this.getId(),
            this.getUser().getId(),
            this.getName(),
            this.getEstimatedValue(),
            this.getAcquisitionDate(),
            this.getInterestRate(),
            this.getDescription()
        );

        return asset;
    }

}
