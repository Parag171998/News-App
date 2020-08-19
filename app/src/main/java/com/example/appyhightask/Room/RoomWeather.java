package com.example.appyhightask.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomWeather {

	@PrimaryKey(autoGenerate = true)
	@NonNull
	private int id;

	@ColumnInfo
	private String tempF;

	@NonNull
	public int getId() {
		return id;
	}

	public void setId(@NonNull int id) {
		this.id = id;
	}

	public String getTempF() {
		return tempF;
	}

	public void setTempF(String tempF) {
		this.tempF = tempF;
	}

	public String getSummery() {
		return summery;
	}

	public void setSummery(String summery) {
		this.summery = summery;
	}

	public Integer getTempC() {
		return tempC;
	}

	public void setTempC(Integer tempC) {
		this.tempC = tempC;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@ColumnInfo
	private String summery;
	@ColumnInfo
	private Integer tempC;
	@ColumnInfo
	private String country;

	public RoomWeather(String tempF, String summery, Integer tempC, String country) {
		this.tempF = tempF;
		this.summery = summery;
		this.tempC = tempC;
		this.country = country;
	}
}
