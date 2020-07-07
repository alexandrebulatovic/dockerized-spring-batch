package com.JCG.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Data {

	@Id
	int id;

	@NonNull
	String firstName;

	@NonNull
	String lastName;

	@NonNull
	String email;

	@NonNull
	String gender;

	@NonNull
	String ipAddress;

}