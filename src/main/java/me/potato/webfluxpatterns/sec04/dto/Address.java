package me.potato.webfluxpatterns.sec04.dto;

import lombok.Data;

@Data
public class Address{
	private String zipCode;
	private String city;
	private String street;
	private String state;
}