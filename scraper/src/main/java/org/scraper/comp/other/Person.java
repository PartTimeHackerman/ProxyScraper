package org.scraper.comp.other;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;

public class Person implements Serializable {

	static final long serialVersionUID = 42L;

	private String email = "";

	private String password = "";

	private String name = "";

	private String adress = "";

	private String city = "";

	private String state = "";

	private String zip = "";

	private String phone = "";

	private String card = "";

	private String cardDate = "";

	private boolean signed;

	private String joinDate="";

	private boolean haveDolans;

	public Person(){
		dlPerson();
	}

	private void dlPerson(){
		try {
			String url = "http://www.fakenamegenerator.com/gen-random-gr-gr.php";
			Document doc = Jsoup.connect(url).userAgent("Mozilla").get();


			name = doc.select("#details > div.content > div.info > div > div.address > h3").text();

			/*String[] nameArr = fullName.split(" ");

			name+=nameArr[0] +" ";
			name+=nameArr[2];

			String fullAddr = doc.select("#details > div.content > div.info > div > div.address > div").text();


			String[] fullArr = fullAddr.split(",");

			String[] addr = fullArr[0].split(" ");
			for (int i = 0; i < addr.length-1; i++) {
				adress+=addr[i]+" ";
			}

			city+=addr[addr.length-1];

			String[] stzip = fullArr[1].split(" ");
			state+=stzip[1];

			zip+=stzip[2];
*/
			email = doc.select("#details > div.content > div.info > div > div.extra > dl:nth-child(11) > dd").text().split(" ")[0];
			//email = email.split("@")[0]+"@cuvox.de";
/*
			card = doc.select("#details > div.content > div.info > div > div.extra > dl:nth-child(18) > dd").text();

			cardDate = doc.select("#details > div.content > div.info > div > div.extra > dl:nth-child(19) > dd").text();

			phone = "1"+doc.select("#details > div.content > div.info > div > div.extra > dl:nth-child(5) > dd").text().replace("-","");

			password = name.replace(" ","").toLowerCase() + "123";*/

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getCardDate() {
		return cardDate;
	}

	public void setCardDate(String cardDate) {
		this.cardDate = cardDate;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public boolean isHaveDolans() {
		return haveDolans;
	}

	public void setHaveDolans(boolean haveDolans) {
		this.haveDolans = haveDolans;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
}
