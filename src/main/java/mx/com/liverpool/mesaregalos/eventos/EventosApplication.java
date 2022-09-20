package mx.com.liverpool.mesaregalos.eventos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.xml.sax.SAXException;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;

public class EventosApplication {
	private static String[] estados = new String[] { "AGS", "AGUASCALIENTES", "Aguascalientes", "aguascalientes ",
			"BAJA CALIFORNIA", "BAJA CALIFORNIA SUR", "BC", "CAMPECHE", "CDMX", "CHI", "CHIAPAS", "Chiapas", "chiapas",
			"CHIHUAHUA", "Chihuahua", "CHIHUAHUA ", "CHS", "Ciudad de México", "ciudad de México",
			"Ciudad de México / Zona Metropolitana", "CMP", "COA", "COAHUILA", "COAHUILA DE ZARAGOZA", "COLIMA", "DF",
			"DGO", "DISTRITO FEDERAL", "DISTRITO FEDERAl", "Distrito Federal", "Distrito federal", "Distrito Federal ",
			"DURANGO", "Durango", "EM", "GRO", "GTO", "GUANAJUATO", "Guanajuato", "GUERRERO", "Guerrero", "HGO",
			"HIDALGO", "JAL", "JALISCO", "Jalisco", "MCH", "MEX", "MEXICO", "Mexico", "México", "MEXICO ", "MIC",
			"MICHOACAN", "MICHOACÁN", "MICHOACAN DE OCAMPO", "MOR", "MORELOS", "Morelos", "NAY", "NAYARIT", "NL",
			"NUEVO LEON", "NUEVO LEÓN", "Nuevo León", "OAX", "OAXACA", "PUE", "PUEBLA", "QR", "QRO", "QUERETARO",
			"QUERÉTARO", "Querétaro", "QUINTANA ROO", "Quintana Roo", "SAN LUIS POTOSI", "SAN LUIS POTOSÍ", "SIN",
			"SINALOA", "SINALOA ", "SLP", "SON", "SONORA", "Sonora", "TAB", "TABASCO", "Tabasco", "TAM", "TAMAULIPAS",
			"Tamaulipas", "TAMAULIPAS ", "TLAXCALA", "TLX", "TMS", "VER", "VERACRUZ", "Veracruz", "veracruz",
			"VERACRUZ DE IGNACIO DE LA LLAV", "YUC", "YUCATAN", "YUCATÁN", "YUCATAN ", "ZAC", "ZACATECAS", "Zacatecas",
			"VERACRUZ DE IGNACIO ", "VERACRUZ DE IGNACIO" };
	private static String recipient_index_2;
	private static StringBuffer reporte;
	private static boolean noEnviar = false;
	private static List<String> noEnviados;
	private static SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
	private static Schema schema;
	private static String eventA = "";

	public static void main(String[] args) throws Throwable {
		noEnviados = new ArrayList();
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader bEnter = new BufferedReader(isr);
		System.out.println("Enter ActionType:");
		String aType = bEnter.readLine();
		System.out.println("Enter condicion de Primary:");
		String bandera = bEnter.readLine();
		System.out.println("-------- Oracle JDBC Connection Testing ------");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException var80) {
			System.out.println("Where is your Oracle JDBC Driver?");
			var80.printStackTrace();
			return;
		}

		System.out.println("Oracle JDBC Driver Registered!");
		Connection connection = null;

		try {
			Resource resource = new ClassPathResource("/application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			String url = props.getProperty("url");
			String usuario = props.getProperty("usuario");
			String constrasenia = props.getProperty("contrasenia");
			connection = DriverManager.getConnection(url, usuario, constrasenia);
			Statement statement = connection.createStatement();
			String consulta = "";
			String fileName = Paths.get(System.getProperty("user.home") + "\\Desktop\\eventos.txt").toString();
			new StringBuffer();
			String event_id = "";
			String recipient_index = "";
			int cont = 0;
			String var14 = "";

			try {
				Throwable var15 = null;
				Object var16 = null;

				try {
					BufferedReader br = new BufferedReader(new FileReader(fileName));

					try {
						label1024: while (true) {
							String line;
							if ((line = br.readLine()) == null) {
								System.out.println("No enviados__________________________________");
								Iterator var87 = noEnviados.iterator();

								while (true) {
									if (!var87.hasNext()) {
										break label1024;
									}

									String evento = (String) var87.next();
									System.out.println(evento);
								}
							}

							System.out.println("Evento=" + line);
							cont = 0;
							consulta = "select event.event_id,          substr(event.event_name,0,20) as event_name,          event.event_type,           to_char (event.event_date,'YYYY-MM-DD') as event_date,          event.preferred_store_id,          event.status,                  to_char (event.creation_date,'YYYY-MM-DD') as creation_date,          event.brand_name,                  CMS.owner_id,          CMS.is_coowner,          SUBSTR(RTRIM(CMS.first_name),0,20) as first_name,          RTRIM(CMS.last_name) as lastNameOrFatherName,          RTRIM(CMS.mother_name) as mother_name,          substr(RTRIM(CMS.nick_name),0,20) as nick_name,                  substr(CMS.email,0.50) as email,          to_char (CMS.date_of_birth,'YYYY-MM-DD') as date_of_birth,          CMS.phone,          CMS.sex,                  evd.EVENT_ADDRESS_ID,          CMS.recipient_index,          substr(RTRIM(EVD.addr_nick_name),0,20) as nick_name_dir,          SUBSTR(RTRIM(EVD.first_name),0,20) as first_name_dir,          RTRIM(EVD.last_name) as last_name_dir,          RTRIM(EVD.maternal_name) as mother_name_dir,                  substr(evd.country,0,6) as country,          substr(evd.city,0,50) as city,          substr(evd.state_id,0,4) as state_id,          substr(evd.state,0,20) as state,          substr(evd.deleg_municipality,0,50) as deleg_municipality,          substr(evd.deleg_municipality_id,0,4) as deleg_municipality_id,                  substr(evd.postal_code,0,6) as postal_code,          substr(evd.neighbourhood,0,254) as neighbourhood,          substr(evd.NEIGHBORHOOD_ID,0,4) as NEIGHBORHOOD_ID,          substr(evd.address1,0,150) as address1,          substr(evd.address2,0,40) as address2,          trim(evd.exterior_number) as exterior_number,                  trim(evd.INTERIOR_NUMBER) as interior_number,                  evd.particular_phone_code,                  evd.phone_number,          substr(evd.other_colony,0,40) as other_colony,         trim(evd.building) building, evd.cellular from mesacore.lp_event event inner join mesacore.LP_EVENT_ADDRESSES EVDS on evds.event_id = event.event_id inner JOIN mesacore.LP_EVENT_ADDRESS EVD     ON (EVDS.EVENT_ADDRESS_ID = EVD.EVENT_ADDRESS_ID) inner JOIN mesacore.LP_EVENT_CELEBRITY EVC     ON (EVC.EVENT_ADDRESS_ID = EVD.EVENT_ADDRESS_ID) JOIN  mesacore.LP_EVENT_OWNER_DETAILS CMS     ON (CMS.owner_id = evc.celebrity_id) where event.event_id = '"
									+ line + "' order by CMS.recipient_index asc";
							System.out.println(consulta);
							ResultSet rs = statement.executeQuery(consulta);
							Evento evento = new Evento();
							List<Owner> owners = new ArrayList();

							ArrayList direcciones;
							Owner owner;
							for (direcciones = new ArrayList(); rs.next(); ++cont) {
								owner = new Owner();
								PreferredAddress direccion = new PreferredAddress();
								direcciones = new ArrayList();
								event_id = rs.getString("event_id");
								String event_name = rs.getString("event_name");
								String event_type = rs.getString("event_type");
								String event_date = rs.getString("event_date");
								String preferred_store_id = rs.getString("preferred_store_id");
								String status = rs.getString("status");
								String creation_date = rs.getString("creation_date");
								String brand_name = rs.getString("brand_name");
								if (cont == 0) {
									event_name = limpiaEventName(event_name);
									evento.setEventId(event_id);
									evento.setEventName(event_name.trim());
									evento.setActionType(aType);
									evento.setEventType(event_type);
									evento.setEventCategory("0");
									evento.setEventDate(event_date);
									evento.setPreferredStore(preferred_store_id);
									evento.setStatus(status);
									evento.setCreationDate(creation_date);
									evento.setBrandName(brand_name);
								}

								String owner_id = rs.getString("owner_id");
								String is_coowner = rs.getString("is_coowner");
								String first_name = rs.getString("first_name");
								String lastNameOrFatherName = rs.getString("lastNameOrFatherName");
								String mother_name = rs.getString("mother_name");
								String nick_name = rs.getString("nick_name");
								String email = rs.getString("email");
								String date_of_birth = rs.getString("date_of_birth");
								String phone = rs.getString("phone");
								String sex = rs.getString("sex");
								owner.setOwnerId(owner_id);
								if (nick_name == null) {
									nick_name = "";
								}

								if (nick_name.length() > 20) {
									nick_name = nick_name.substring(0, 20);
								}

								if (nick_name != null) {
									nick_name = limpiaEventName(nick_name);
								}

								if (mother_name == null) {
									mother_name = "";
								}

								if (lastNameOrFatherName != null) {
									lastNameOrFatherName = limpiaEventName(lastNameOrFatherName);
								}

								if (email == null) {
									email = "";
								}

								if (first_name == null) {
									first_name = "";
								} else {
									first_name = first_name.length() > 20 ? limpiaEventName(first_name.substring(0, 20))
											: limpiaEventName(first_name);
								}

								owner.setOwnerType("Primary");
								owner.setNickName(limpiaEventName(nick_name));
								owner.setFirstName(first_name);
								owner.setLastNameOrFatherName(lastNameOrFatherName == null ? owner_id
										: limpiaEventName(lastNameOrFatherName));
								owner.setMotherName(limpiaEventName(mother_name));
								owner.setEmail(email);
								owner.setDateOfBirth(date_of_birth);
								owner.setPhone(phone);
								if (sex != null) {
									owner.setGender(sex.equals("1") ? "F" : "M");
								} else {
									owner.setGender("M");
								}

								if (owner.getLastNameOrFatherName().length() >= 20) {
									owner.setLastNameOrFatherName(owner.getLastNameOrFatherName().substring(0, 20));
								}

								String event_address_id = rs.getString("EVENT_ADDRESS_ID");
								recipient_index = rs.getString("recipient_index");
								String nick_name_dir = rs.getString("nick_name_dir");
								String first_name_dir = rs.getString("first_name_dir");
								String last_name_dir = rs.getString("last_name_dir");
								String mother_name_dir = rs.getString("mother_name_dir");
								String country = rs.getString("country");
								String city = rs.getString("city");
								String state_id = rs.getString("state_id");
								String state = rs.getString("state");
								String deleg_municipality = rs.getString("deleg_municipality");
								String deleg_municipality_id = rs.getString("deleg_municipality_id");
								String postal_code = rs.getString("postal_code");
								String neighbourhood = rs.getString("neighbourhood");
								String address1 = rs.getString("address1");
								String address2 = rs.getString("address2");
								String exterior_number = rs.getString("exterior_number");
								String interior_number = rs.getString("interior_number");
								String particular_phone_code = rs.getString("particular_phone_code");
								String phone_number = rs.getString("phone_number");
								String other_colony = rs.getString("other_colony");
								String neighbourhood_id = rs.getString("NEIGHBORHOOD_ID");
								String celular = rs.getString("cellular");
								String building = rs.getString("building");
								if (state != null) {
									if (state.length() > 20) {
										state = state.substring(0, 19);
									}
								} else {
									state = "DISTRITO FEDERAL";
								}

								if (deleg_municipality != null) {
									if (deleg_municipality.length() > 30) {
										deleg_municipality = deleg_municipality.substring(0, 29);
									}
								} else {
									deleg_municipality = "Delegacion";
								}

								if (address1 != null) {
									address1 = limpiaEventName(address1);
									if (address1.length() > 40) {
										address1 = limpiaEventName(address1.substring(0, 39));
									}
								} else {
									address1 = "address1";
								}

								if (address2 != null) {
									address2 = limpiaEventName(address2);
									if (address2.length() > 40) {
										address2 = limpiaEventName(address2.substring(0, 39));
									}
								} else {
									address2 = "NA";
								}

								if (neighbourhood != null) {
									if (neighbourhood.length() > 30) {
										neighbourhood = neighbourhood.substring(0, 29);
									}
								} else {
									neighbourhood = "neighbourhood";
								}

								if (nick_name_dir != null) {
									nick_name_dir = limpiaEventName(nick_name_dir);
								} else {
									nick_name_dir = "";
								}

								if (building == null) {
									direccion.setBuilding("");
								} else {
									direccion
											.setBuilding(building.length() > 20 ? building.substring(0, 20) : building);
								}

								if (interior_number == null) {
									direccion.setInteriorNumber("0");
								} else {
									direccion.setInteriorNumber(
											interior_number.length() > 15 ? interior_number.substring(0, 15)
													: interior_number);
								}

								if (exterior_number == null) {
									direccion.setExteriorNumber("0");
								} else {
									direccion.setExteriorNumber(
											exterior_number.length() > 15 ? exterior_number.substring(0, 15)
													: exterior_number);
								}

								if (buscaEstado(state)) {
									direccion.setStateId(state_id == null ? "00" : state_id);
									direccion.setState(state);
									direccion.setDelegationMunicipality(deleg_municipality);
									if (deleg_municipality_id != null) {
										if (deleg_municipality_id.length() > 4) {
											direccion.setDelegationMunicipalityId(
													deleg_municipality_id.substring(deleg_municipality_id.length() - 5,
															deleg_municipality_id.length() - 1));
										} else {
											direccion.setDelegationMunicipalityId(
													String.format("%04d", Integer.parseInt(deleg_municipality_id)));
										}
									} else {
										direccion.setDelegationMunicipalityId("0000");
									}
								} else {
									direccion.setStateId(deleg_municipality_id == null ? "00" : deleg_municipality_id);
									direccion.setState(deleg_municipality);
									direccion.setDelegationMunicipality(state);
									if (state_id != null) {
										if (state_id.length() > 4) {
											direccion.setDelegationMunicipalityId(
													state_id.substring(state_id.length() - 5, state_id.length() - 1));
										} else {
											direccion.setDelegationMunicipalityId(
													String.format("%04d", Integer.parseInt(state_id)));
										}
									} else {
										direccion.setDelegationMunicipalityId("0000");
									}
								}

								if (first_name_dir == null) {
									first_name_dir = "";
								} else {
									first_name_dir = first_name_dir.length() > 20
											? limpiaEventName(first_name_dir.substring(0, 20))
											: limpiaEventName(first_name_dir);
								}

								direccion.setAddress1(limpiaEventName(address1));
								direccion.setAddress2(limpiaEventName(address2));
								direccion.setAddressId(event_address_id);
								direccion.setCity(city == null ? "CDMX" : limpiaEventName(city));
								direccion.setCountry(country == null ? "Mexico" : limpiaEventName(country));
								direccion.setEventRecipientIndex(recipient_index == null ? "1" : recipient_index);
								System.out.println("recipient_index=>" + recipient_index);
								direccion.setFirstName(first_name_dir);
								direccion.setLastNameOrFatherName(limpiaEventName(last_name_dir));
								direccion.setMaternalName(
										mother_name_dir == null ? "" : limpiaEventName(mother_name_dir));
								direccion.setNeighbourhood(limpiaEventName(neighbourhood));
								if (neighbourhood_id != null) {
									if (neighbourhood_id.length() > 4) {
										direccion.setNeighbourhoodId(neighbourhood_id.substring(
												neighbourhood_id.length() - 5, neighbourhood_id.length() - 1));
									} else if (neighbourhood_id.equals("-2")) {
										direccion.setNeighbourhoodId("0000");
									} else if (neighbourhood_id.matches("\\d*")) {
										direccion.setNeighbourhoodId(
												String.format("%04d", Integer.parseInt(neighbourhood_id)));
									} else {
										direccion.setNeighbourhoodId("0000");
									}
								} else {
									direccion.setNeighbourhoodId("0000");
								}

								if (nick_name_dir != null) {
									if (nick_name_dir.length() > 20) {
										nick_name_dir = nick_name_dir.substring(0, 20);
									}
								} else {
									nick_name_dir = "NA";
								}

								direccion.setNickName(limpiaEventName(nick_name_dir));
								direccion.setOtherColony(other_colony == null ? "" : limpiaEventName(other_colony));
								direccion.setParticularPhoneCode(
										particular_phone_code == null ? "00" : particular_phone_code);
								direccion.setPhoneNumber(phone_number == null ? "00000000" : phone_number);
								direccion.setPostalcode(postal_code == null ? "00000" : postal_code);
								direcciones.add(direccion);
								owner.setPreferredAddresses(direcciones);
								owners.add(owner);
							}

							if (bandera.equals("1")) {
								owners.remove(1);
							}

							if (owners.size() == 1) {
								if (((PreferredAddress) ((Owner) owners.get(0)).getPreferredAddresses().get(0))
										.getEventRecipientIndex().equals("2")) {
									((PreferredAddress) ((Owner) owners.get(0)).getPreferredAddresses().get(0))
											.setEventRecipientIndex("1");
								}

								owner = consultarOwnersFaltantes(connection, event_id, recipient_index);
								if (owner != null) {
									List<PreferredAddress> direcciones2 = new ArrayList();
									Iterator var90 = direcciones.iterator();

									while (var90.hasNext()) {
										PreferredAddress dir = (PreferredAddress) var90.next();
										direcciones2.add((PreferredAddress) dir.clone());
									}

									owner.setPreferredAddresses(direcciones2);
									owners.add(owner);
									((PreferredAddress) ((Owner) owners.get(1)).getPreferredAddresses().get(0))
											.setEventRecipientIndex(recipient_index_2);
								}
							}

							evento.setEventOwnerDetails(owners);
							if (evento.getEventId() != null) {
								jaxbObjectToXML(evento);
							} else {
								System.out.println(line + " - Evento se envia a reproceso de adiconal");
								armaXMLAdicional(connection, line, aType);
							}
						}
					} finally {
						if (br != null) {
							br.close();
						}

					}
				} catch (Throwable var82) {
					if (var15 == null) {
						var15 = var82;
					} else if (var15 != var82) {
						var15.addSuppressed(var82);
					}

					throw var15;
				}
			} catch (CloneNotSupportedException | IOException var83) {
				var83.printStackTrace();
			}
		} catch (SQLException var84) {
			try {
				connection.close();
			} catch (SQLException var78) {
				var78.printStackTrace();
			}

			System.out.println("Connection Failed! Check output console");
			var84.printStackTrace();
			return;
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException var79) {
				var79.printStackTrace();
			}

			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

	}

	private static String jaxbObjectToXML(Evento evento) {
		String xmlString = "";

		try {
			schema = factory.newSchema(EventosApplication.class.getResource("/validateXMLSOMS.xsd"));
			JAXBContext context = JAXBContext.newInstance(Evento.class);
			Marshaller m = context.createMarshaller();
			m.setProperty("jaxb.formatted.output", Boolean.TRUE);
			m.setSchema(schema);
			m.setEventHandler((handler) -> {
				if (eventA.equals("")) {
					eventA = evento.getEventId() + "-" + handler.getLinkedException().getLocalizedMessage();
				}

				noEnviar = true;
				return true;
			});
			StringWriter sw = new StringWriter();
			m.marshal(evento, sw);
			if (!noEnviar) {
				xmlString = sw.toString();
				xmlString = quitarAcentos(xmlString);
				System.out.println(xmlString);
				mqSendPROD(xmlString);
			} else {
				noEnviados.add(eventA);
			}

			noEnviar = false;
			context = null;
			xmlString = null;
			m = null;
			sw = null;
			schema = null;
			eventA = "";
			System.gc();
		} catch (SAXException | JAXBException var5) {
			var5.printStackTrace();
		}

		return xmlString;
	}

	public static void mqSend(String mensaje) {
		try {
			JmsFactoryFactory ff = JmsFactoryFactory.getInstance("com.ibm.msg.client.wmq");
			JmsConnectionFactory cf = ff.createConnectionFactory();
			cf.setStringProperty("XMSC_WMQ_HOST_NAME", "172.16.203.141");
			cf.setIntProperty("XMSC_WMQ_PORT", 1417);
			cf.setStringProperty("XMSC_WMQ_CHANNEL", "CH.T.ATG.WMB.SVRCONN");
			cf.setIntProperty("XMSC_WMQ_CONNECTION_MODE", 1);
			cf.setStringProperty("XMSC_WMQ_QUEUE_MANAGER", "QM.WBI.BRK.QA.03");
			cf.setStringProperty("XMSC_WMQ_APPNAME", "MesaRegalos (JMS)");
			cf.setBooleanProperty("XMSC_USER_AUTHENTICATION_MQCSP", true);
			javax.jms.Connection connection = cf.createConnection();
			connection.start();
			Session session = connection.createSession(false, 1);
			Destination destination = session.createQueue("QA.ACTUALIZADATOS.GR.SOMS.IN");
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(1);
			TextMessage message = session.createTextMessage(mensaje);
			producer.send(message);
			session.close();
			connection.close();
			System.out.println("Message sent QA");
		} catch (JMSException var8) {
			var8.printStackTrace();
		}

	}

	public static void mqSendPROD(String mensaje) {
		try {
			JmsFactoryFactory ff = JmsFactoryFactory.getInstance("com.ibm.msg.client.wmq");
			JmsConnectionFactory cf = ff.createConnectionFactory();
			cf.setStringProperty("XMSC_WMQ_HOST_NAME", "172.27.203.201");
			cf.setIntProperty("XMSC_WMQ_PORT", 1421);
			cf.setStringProperty("XMSC_WMQ_CHANNEL", "CH.P.ATG.WMB.SVRCONN");
			cf.setIntProperty("XMSC_WMQ_CONNECTION_MODE", 1);
			cf.setStringProperty("XMSC_WMQ_QUEUE_MANAGER", "QM.WBI.BRK.PROD.03");
			cf.setStringProperty("XMSC_WMQ_APPNAME", "MesaRegalos (JMS)");
			cf.setBooleanProperty("XMSC_USER_AUTHENTICATION_MQCSP", true);
			javax.jms.Connection connection = cf.createConnection();
			connection.start();
			Session session = connection.createSession(false, 1);
			Destination destination = session.createQueue("QA.ACTUALIZADATOS.GR.SOMS.IN");
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(1);
			TextMessage message = session.createTextMessage(mensaje);
			producer.send(message);
			session.close();
			connection.close();
			System.out.println("Message sent PROD");
		} catch (JMSException var8) {
			var8.printStackTrace();
		}

	}

	public static String quitarAcentos(String cadena) {
		return cadena.replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
				.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
				.replace("ñ", "n").replace("Ñ", "N").replace("&amp;", "Y").replace("#", "Y");
	}

	private static boolean buscaEstado(String estadoB) {
		boolean respuesta = false;

		for (int i = 0; i < estados.length; ++i) {
			if (estados[i].equals(estadoB)) {
				respuesta = true;
			}
		}

		return respuesta;
	}

	private static String limpiaEventName(String eventName) {
		eventName = quitarAcentos(eventName);
		Pattern pattern = Pattern.compile("\\&\\#([^\\\"]*)\\;+");
		Matcher matcher = pattern.matcher(eventName);
		if (matcher.find()) {
			eventName = eventName.replace(matcher.group(1), "");
			eventName = eventName.replace("&#;", "");
			System.out.println("eventName Despues => " + eventName);
		} else {
			pattern = Pattern.compile("[^a-zA-Z0-9\\s]+");

			for (matcher = pattern.matcher(eventName); matcher
					.find(); eventName = eventName.replace(matcher.group(), "")) {
			}
		}

		return eventName;
	}

	private static Owner consultarOwnersFaltantes(Connection conexion, String evento, String recipientIndex)
			throws SQLException {
		Statement statement = conexion.createStatement();
		String consulta = "select   CMS.owner_id,              CMS.is_coowner,          RTRIM(CMS.first_name) as first_name,          RTRIM(CMS.last_name) as lastNameOrFatherName,              RTRIM(CMS.mother_name) as mother_name,          substr(RTRIM(CMS.nick_name),0,20) as nick_name,                     substr(CMS.email,0.50) as email,          to_char (CMS.date_of_birth,'YYYY-MM-DD') as date_of_birth,             CMS.phone,          CMS.sex,                  CMS.recipient_index from mesacore.lp_event event JOIN  mesacore.LP_EVENT_OWNER_DETAILS CMS     ON (CMS.event_id = event.event_id) where event.event_id = '"
				+ evento + "' " + "and CMS.recipient_index != " + recipientIndex + " order by CMS.recipient_index asc";
		System.out.println(consulta);
		ResultSet rs = statement.executeQuery(consulta);
		Owner owner = new Owner();
		if (!rs.isBeforeFirst()) {
			owner = null;
		}

		while (rs.next()) {
			String owner_id = rs.getString("owner_id");
			String is_coowner = rs.getString("is_coowner");
			String first_name = rs.getString("first_name");
			String lastNameOrFatherName = rs.getString("lastNameOrFatherName");
			String mother_name = rs.getString("mother_name");
			String nick_name = rs.getString("nick_name");
			String email = rs.getString("email");
			String date_of_birth = rs.getString("date_of_birth");
			String phone = rs.getString("phone");
			String sex = rs.getString("sex");
			recipient_index_2 = rs.getString("recipient_index");
			owner.setOwnerId(owner_id);
			if (nick_name == null) {
				nick_name = "";
			}

			if (nick_name.length() > 20) {
				nick_name = nick_name.substring(0, 20);
			}

			if (recipient_index_2.equals("2")) {
				owner.setOwnerType("Coowner");
			} else {
				owner.setOwnerType("Primary");
			}

			if (email == null) {
				email = "";
			}

			if (first_name == null) {
				first_name = "";
			} else {
				first_name = first_name.length() > 20 ? limpiaEventName(first_name.substring(0, 20))
						: limpiaEventName(first_name);
			}

			owner.setOwnerType("Coowner");
			recipient_index_2 = "2";
			owner.setNickName(nick_name);
			owner.setFirstName(first_name == null ? owner_id : first_name);
			owner.setLastNameOrFatherName(lastNameOrFatherName == null ? owner_id : lastNameOrFatherName);
			owner.setMotherName(mother_name);
			owner.setEmail(email);
			owner.setDateOfBirth(date_of_birth);
			owner.setPhone(phone);
			if (sex != null) {
				owner.setGender(sex.equals("1") ? "F" : "M");
			} else {
				owner.setGender("M");
			}
		}

		rs.close();
		statement.close();
		return owner;
	}

	private static void consultaEventosDiasAnteriorCreados(Connection conexion) throws SQLException {
		System.out.println("Se inicia consulta de eventos creados");
		reporte = new StringBuffer();
		Statement statement = conexion.createStatement();
		String consulta = "select event_id || '@@@@\"\"' as evento\r\nfrom mesacore.lp_event\r\nwhere creation_date between '25/09/19' and '25/09/19'";
		System.out.println(consulta);
		ResultSet rs = statement.executeQuery(consulta);

		while (rs.next()) {
			String evento = rs.getString("evento");
			reporte.append(evento + "\r\n");
		}

		rs.close();
		statement.close();

		try {
			Files.write(Paths.get("C:\\Users\\sdmurop\\Desktop\\exceles\\unico.csv"),
					String.valueOf(reporte).getBytes(), new OpenOption[0]);
		} catch (IOException var5) {
			rs.close();
			statement.close();
			var5.printStackTrace();
		}

		System.out.println("Se termino consulta de eventos creados");
	}

	private static void consultaEventosDiasAnteriorActualizados(Connection conexion) throws SQLException {
		System.out.println("Se inicia consulta de eventos actualizados");
		reporte = new StringBuffer();
		Statement statement = conexion.createStatement();
		String consulta = "select event_id || '@@@@\"\"' as evento\r\nfrom mesacore.lp_event\r\nwhere to_char(last_modified_date,'dd/mm/yy') = '04/09/19'";
		System.out.println(consulta);
		ResultSet rs = statement.executeQuery(consulta);

		while (rs.next()) {
			String evento = rs.getString("evento");
			reporte.append(evento + "\r\n");
		}

		rs.close();
		statement.close();

		try {
			Files.write(Paths.get("C:\\Users\\sdmurop\\Desktop\\exceles\\unico.csv"),
					String.valueOf(reporte).getBytes(), new OpenOption[0]);
		} catch (IOException var5) {
			rs.close();
			statement.close();
			var5.printStackTrace();
		}

		System.out.println("Se termino consulta de eventos actualizados");
	}

	private static void armaXMLAdicional(Connection connection, String eventoBuscar, String aType)
			throws SQLException, CloneNotSupportedException {
		String recipient_index = "";
		int cont = 0;
		String consulta = "";
		String event_id = "";
		Statement statement = connection.createStatement();
		consulta = "select event.event_id,          substr(event.event_name,0,20) as event_name,          event.event_type,           to_char (event.event_date,'YYYY-MM-DD') as event_date,          event.preferred_store_id,          event.status,                  to_char (event.creation_date,'YYYY-MM-DD') as creation_date,          event.brand_name,                  CMS.owner_id,          CMS.is_coowner,          SUBSTR(RTRIM(CMS.first_name),0,20) as first_name,          RTRIM(CMS.last_name) as lastNameOrFatherName,          RTRIM(CMS.mother_name) as mother_name,          substr(RTRIM(CMS.nick_name),0,20) as nick_name,                  substr(CMS.email,0.50) as email,          to_char (CMS.date_of_birth,'YYYY-MM-DD') as date_of_birth,          CMS.phone,          CMS.sex,                  evd.EVENT_ADDRESS_ID,          CMS.recipient_index,          substr(RTRIM(EVD.addr_nick_name),0,20) as nick_name_dir,          SUBSTR(RTRIM(EVD.first_name),0,20) as first_name_dir,          RTRIM(EVD.last_name) as last_name_dir,          RTRIM(EVD.maternal_name) as mother_name_dir,                  substr(evd.country,0,6) as country,          substr(evd.city,0,50) as city,          substr(evd.state_id,0,4) as state_id,          substr(evd.state,0,20) as state,          substr(evd.deleg_municipality,0,50) as deleg_municipality,          substr(evd.deleg_municipality_id,0,4) as deleg_municipality_id,                  substr(evd.postal_code,0,6) as postal_code,          substr(evd.neighbourhood,0,50) as neighbourhood,          substr(evd.NEIGHBORHOOD_ID,0,4) as NEIGHBORHOOD_ID,          substr(evd.address1,0,40) as address1,          substr(evd.address2,0,40) as address2,          trim(evd.exterior_number) as exterior_number,                  trim(evd.INTERIOR_NUMBER) as interior_number,                  evd.particular_phone_code,                  evd.phone_number,          substr(evd.other_colony,0,40) as other_colony,         trim(evd.building) building, evd.cellular from mesacore.lp_event event inner join MESACORE.LP_EVENT_ADDRESSES EVDS on evds.event_id = event.event_id inner JOIN MESACORE.LP_EVENT_ADDRESS EVD     ON (EVDS.EVENT_ADDRESS_ID = EVD.EVENT_ADDRESS_ID) JOIN  MESACORE.LP_EVENT_OWNER_DETAILS CMS     ON (CMS.event_id = event.event_id) where event.event_id = '"
				+ eventoBuscar + "' order by CMS.recipient_index asc";
		System.out.println(consulta);
		ResultSet rs = statement.executeQuery(consulta);
		Evento evento = new Evento();
		List<Owner> owners = new ArrayList();

		ArrayList direcciones;
		Owner owner;
		for (direcciones = new ArrayList(); rs.next(); ++cont) {
			owner = new Owner();
			PreferredAddress direccion = new PreferredAddress();
			direcciones = new ArrayList();
			event_id = rs.getString("event_id");
			String event_name = rs.getString("event_name");
			String event_type = rs.getString("event_type");
			String event_date = rs.getString("event_date");
			String preferred_store_id = rs.getString("preferred_store_id");
			String status = rs.getString("status");
			String creation_date = rs.getString("creation_date");
			String brand_name = rs.getString("brand_name");
			if (cont == 0) {
				event_name = limpiaEventName(event_name);
				evento.setEventId(event_id);
				evento.setEventName(event_name.trim());
				evento.setActionType(aType);
				evento.setEventType(event_type);
				evento.setEventCategory("0");
				evento.setEventDate(event_date);
				evento.setPreferredStore(preferred_store_id);
				evento.setStatus(status);
				evento.setCreationDate(creation_date);
				evento.setBrandName(brand_name);
			}

			String owner_id = rs.getString("owner_id");
			String is_coowner = rs.getString("is_coowner");
			String first_name = rs.getString("first_name");
			String lastNameOrFatherName = rs.getString("lastNameOrFatherName");
			String mother_name = rs.getString("mother_name");
			String nick_name = rs.getString("nick_name");
			String email = rs.getString("email");
			String date_of_birth = rs.getString("date_of_birth");
			String phone = rs.getString("phone");
			String sex = rs.getString("sex");
			owner.setOwnerId(owner_id);
			owner.setOwnerType(is_coowner.equals("0") ? "Primary" : "Coowner");
			if (nick_name == null) {
				nick_name = "";
			}

			if (nick_name.length() > 20) {
				nick_name = nick_name.substring(0, 20);
			}

			if (nick_name != null) {
				nick_name = limpiaEventName(nick_name);
			}

			if (mother_name == null) {
				mother_name = "";
			}

			if (lastNameOrFatherName != null) {
				lastNameOrFatherName = limpiaEventName(lastNameOrFatherName);
			}

			if (email == null) {
				email = "";
			}

			if (first_name == null) {
				first_name = "";
			} else {
				first_name = first_name.length() > 20 ? limpiaEventName(first_name.substring(0, 20))
						: limpiaEventName(first_name);
			}

			owner.setNickName(limpiaEventName(nick_name));
			owner.setFirstName(first_name);
			owner.setLastNameOrFatherName(
					lastNameOrFatherName == null ? owner_id : limpiaEventName(lastNameOrFatherName));
			owner.setMotherName(limpiaEventName(mother_name));
			owner.setEmail(email);
			owner.setDateOfBirth(date_of_birth);
			owner.setPhone(phone);
			if (sex != null) {
				owner.setGender(sex.equals("1") ? "F" : "M");
			} else {
				owner.setGender("M");
			}

			if (owner.getLastNameOrFatherName().length() >= 20) {
				owner.setLastNameOrFatherName(owner.getLastNameOrFatherName().substring(0, 20));
			}

			String event_address_id = rs.getString("EVENT_ADDRESS_ID");
			recipient_index = rs.getString("recipient_index");
			String nick_name_dir = rs.getString("nick_name_dir");
			String first_name_dir = rs.getString("first_name_dir");
			String last_name_dir = rs.getString("last_name_dir");
			String mother_name_dir = rs.getString("mother_name_dir");
			String country = rs.getString("country");
			String city = rs.getString("city");
			String state_id = rs.getString("state_id");
			String state = rs.getString("state");
			String deleg_municipality = rs.getString("deleg_municipality");
			String deleg_municipality_id = rs.getString("deleg_municipality_id");
			String postal_code = rs.getString("postal_code");
			String neighbourhood = rs.getString("neighbourhood");
			String address1 = rs.getString("address1");
			String address2 = rs.getString("address2");
			String exterior_number = rs.getString("exterior_number");
			String interior_number = rs.getString("interior_number");
			String particular_phone_code = rs.getString("particular_phone_code");
			String phone_number = rs.getString("phone_number");
			String other_colony = rs.getString("other_colony");
			String neighbourhood_id = rs.getString("NEIGHBORHOOD_ID");
			String celular = rs.getString("cellular");
			String building = rs.getString("building");
			if (state != null) {
				if (state.length() > 20) {
					state = state.substring(0, 19);
				}
			} else {
				state = "DISTRITO FEDERAL";
			}

			if (deleg_municipality != null) {
				if (deleg_municipality.length() > 30) {
					deleg_municipality = deleg_municipality.substring(0, 29);
				}
			} else {
				deleg_municipality = "Delegacion";
			}

			if (address1 != null) {
				address1 = limpiaEventName(address1);
				if (address1.length() > 40) {
					address1 = limpiaEventName(address1.substring(0, 39));
				}
			} else {
				address1 = "address1";
			}

			if (address2 != null) {
				address2 = limpiaEventName(address2);
				if (address2.length() > 40) {
					address2 = limpiaEventName(address2.substring(0, 39));
				}
			} else {
				address2 = "NA";
			}

			if (neighbourhood != null) {
				if (neighbourhood.length() > 30) {
					neighbourhood = neighbourhood.substring(0, 29);
				}
			} else {
				neighbourhood = "neighbourhood";
			}

			if (nick_name_dir != null) {
				nick_name_dir = limpiaEventName(nick_name_dir);
			} else {
				nick_name_dir = "";
			}

			if (building == null) {
				direccion.setBuilding("");
			} else {
				direccion.setBuilding(building.length() > 20 ? building.substring(0, 20) : building);
			}

			if (interior_number == null) {
				direccion.setInteriorNumber("0");
			} else {
				direccion.setInteriorNumber(
						interior_number.length() > 15 ? interior_number.substring(0, 15) : interior_number);
			}

			if (exterior_number == null) {
				direccion.setExteriorNumber("0");
			} else {
				direccion.setExteriorNumber(
						exterior_number.length() > 15 ? exterior_number.substring(0, 15) : exterior_number);
			}

			if (buscaEstado(state)) {
				direccion.setStateId(state_id == null ? "00" : state_id);
				direccion.setState(state);
				direccion.setDelegationMunicipality(deleg_municipality);
				if (deleg_municipality_id != null) {
					if (deleg_municipality_id.length() > 4) {
						direccion.setDelegationMunicipalityId(deleg_municipality_id
								.substring(deleg_municipality_id.length() - 5, deleg_municipality_id.length() - 1));
					} else {
						direccion.setDelegationMunicipalityId(
								String.format("%04d", Integer.parseInt(deleg_municipality_id)));
					}
				} else {
					direccion.setDelegationMunicipalityId("0000");
				}
			} else {
				direccion.setStateId(deleg_municipality_id == null ? "00" : deleg_municipality_id);
				direccion.setState(deleg_municipality);
				direccion.setDelegationMunicipality(state);
				if (state_id != null) {
					if (state_id.length() > 4) {
						direccion.setDelegationMunicipalityId(
								state_id.substring(state_id.length() - 5, state_id.length() - 1));
					} else {
						direccion.setDelegationMunicipalityId(String.format("%04d", Integer.parseInt(state_id)));
					}
				} else {
					direccion.setDelegationMunicipalityId("0000");
				}
			}

			if (first_name_dir == null) {
				first_name_dir = "";
			} else {
				first_name_dir = first_name_dir.length() > 20 ? limpiaEventName(first_name_dir.substring(0, 20))
						: limpiaEventName(first_name_dir);
			}

			direccion.setAddress1(limpiaEventName(address1));
			direccion.setAddress2(limpiaEventName(address2));
			direccion.setAddressId(event_address_id);
			direccion.setCity(city == null ? "CDMX" : limpiaEventName(city));
			direccion.setCountry(country == null ? "Mexico" : limpiaEventName(country));
			direccion.setEventRecipientIndex(recipient_index == null ? "1" : recipient_index);
			System.out.println("recipient_index=>" + recipient_index);
			direccion.setFirstName(first_name_dir);
			direccion.setLastNameOrFatherName(limpiaEventName(last_name_dir));
			direccion.setMaternalName(mother_name_dir == null ? "" : limpiaEventName(mother_name_dir));
			direccion.setNeighbourhood(limpiaEventName(neighbourhood));
			if (neighbourhood_id != null) {
				if (neighbourhood_id.length() > 4) {
					direccion.setNeighbourhoodId(
							neighbourhood_id.substring(neighbourhood_id.length() - 5, neighbourhood_id.length() - 1));
				} else if (neighbourhood_id.equals("-2")) {
					direccion.setNeighbourhoodId("0000");
				} else if (neighbourhood_id.matches("\\d*")) {
					direccion.setNeighbourhoodId(String.format("%04d", Integer.parseInt(neighbourhood_id)));
				} else {
					direccion.setNeighbourhoodId("0000");
				}
			} else {
				direccion.setNeighbourhoodId("0000");
			}

			if (nick_name_dir != null) {
				if (nick_name_dir.length() > 20) {
					nick_name_dir = nick_name_dir.substring(0, 20);
				}
			} else {
				nick_name_dir = "NA";
			}

			direccion.setNickName(limpiaEventName(nick_name_dir));
			direccion.setOtherColony(other_colony == null ? "" : limpiaEventName(other_colony));
			direccion.setParticularPhoneCode(particular_phone_code == null ? "00" : particular_phone_code);
			direccion.setPhoneNumber(phone_number == null ? "00000000" : phone_number);
			direccion.setPostalcode(postal_code == null ? "00000" : postal_code);
			direcciones.add(direccion);
			owner.setPreferredAddresses(direcciones);
			owners.add(owner);
		}

		if (owners.size() == 1) {
			if (((PreferredAddress) ((Owner) owners.get(0)).getPreferredAddresses().get(0)).getEventRecipientIndex()
					.equals("2")) {
				((PreferredAddress) ((Owner) owners.get(0)).getPreferredAddresses().get(0)).setEventRecipientIndex("1");
			}

			owner = consultarOwnersFaltantes(connection, event_id, recipient_index);
			if (owner != null) {
				List<PreferredAddress> direcciones2 = new ArrayList();
				Iterator var56 = direcciones.iterator();

				while (var56.hasNext()) {
					PreferredAddress dir = (PreferredAddress) var56.next();
					direcciones2.add((PreferredAddress) dir.clone());
				}

				owner.setPreferredAddresses(direcciones2);
				owners.add(owner);
				((PreferredAddress) ((Owner) owners.get(1)).getPreferredAddresses().get(0))
						.setEventRecipientIndex(recipient_index_2);
			}
		}

		evento.setEventOwnerDetails(owners);
		if (evento.getEventId() != null) {
			jaxbObjectToXML(evento);
		} else {
			noEnviados.add(event_id + evento.getEventType());
		}

	}

	public static String tipoEvento(int tipo) {
		String es = "";
		switch (tipo) {
		case 0:
			es = "Boda";
			break;
		case 1:
			es = "Bebé";
			break;
		case 2:
			es = "XV años";
			break;
		case 3:
			es = "Bar Mitzvah";
			break;
		case 4:
			es = "Bat Mitzvah";
			break;
		case 5:
			es = "Bautizo";
			break;
		case 6:
			es = "Primera Comunión";
			break;
		case 7:
			es = "Confirmación";
			break;
		case 8:
			es = "Otras Ceremonias Religiosas";
			break;
		case 9:
			es = "Fiesta/ Reunión";
			break;
		case 10:
			es = "Fiesta Infantil";
			break;
		case 11:
			es = "Cumpleaños";
			break;
		case 12:
			es = "Despedida";
			break;
		case 13:
			es = "Open House";
			break;
		case 14:
			es = "Aniversario";
			break;
		case 15:
			es = "Otros";
		}

		return es;
	}
}
