package com.team.project.domain.address.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.team.project.domain.user.entity.User;
import org.hibernate.annotations.UuidGenerator;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_user_address")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	// 배송지 소유자
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "address_name", nullable = false, length = 50)
	private String addressName;

	@Column(name = "phone", nullable = false, length = 20)
	private String phone;

	@Column(name = "address", nullable = false, length = 255)
	private String address;

	@Column(name = "detail_address", length = 255)
	private String detailAddress;

	@Column(name = "latitude", precision = 10, scale = 7)
	private BigDecimal latitude;

	@Column(name = "longitude", precision = 10, scale = 7)
	private BigDecimal longitude;

	@Column(name = "is_default", nullable = false)
	private Boolean isDefault = false;

	public UserAddress(User user, String addressName, String phone, String address, String detailAddress,
		BigDecimal latitude,
		BigDecimal longitude, Boolean isDefault) {
		super();

		this.user = user;
		// this.user.addUserAddress(this);

		this.addressName = addressName;
		this.phone = phone;
		this.address = address;
		this.detailAddress = detailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isDefault = isDefault;
	}

	public static UserAddress of(User user, String addressName, String phone, String address, String detailAddress,
		BigDecimal latitude, BigDecimal longitude, Boolean isDefault) {
		return new UserAddress(user, addressName, phone, address, detailAddress, latitude, longitude, isDefault);
	}

	public void markDeleted(UUID deletedBy) {
		super.markDeleted(deletedBy);
	}

	public void updateIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}