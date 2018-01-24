package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModeloTrazaapp {

	private MySQL sql = new MySQL("trazaapp2", "juan", "juan");

	// ------- patron singleton -------
	static ModeloTrazaapp model = null;

	public static ModeloTrazaapp getInstanceTrazaapp() {
		if (model == null) {
			model = new ModeloTrazaapp();
		}
		return model;
	}

	private ModeloTrazaapp() {

	}

	// ------- ++++++++++++++++ -------

	public LoginToken attemptLogin(String email, String password){

		try{
			String query = "SELECT * FROM USERS " +
					"WHERE email='"+ email +"'" +
					"  AND PASSWORD='"+password+"'";
			System.out.println(query);
			ResultSet resultSet = sql.getResultset(query);

			if(resultSet.next()){
				System.out.println("Existe el usuario");
				// conn.close();
				boolean admin = false;
				System.out.println("Es admin:" +resultSet.getString("role_id"));
				if(resultSet.getInt("role_id")==1){
					admin = true;
				}

				Persona usuario = new Persona();
				usuario.setId(resultSet.getInt("id"));
				usuario.setUser(resultSet.getString("username"));
				usuario.setPass(resultSet.getString("password"));
				usuario.setNombre(resultSet.getString("name"));
				usuario.setTelefono(resultSet.getString("phone"));
				usuario.setDireccion(resultSet.getString("address"));
				usuario.setCiudad(resultSet.getInt("location_id"));
				usuario.setAdmin(admin);
				usuario.setEmail(resultSet.getString("email"));
				usuario.setLatitude(resultSet.getDouble("latitude"));
				usuario.setLongitude(resultSet.getDouble("longitude"));
				LoginToken result = new LoginToken();
				result.setMensaje("login Exitoso");
				result.setStatus(200);
				result.setUsuario(usuario);
				return result;
			}
			else{
				System.out.println("usuario no-valido");
				LoginToken result = new LoginToken();
				result.setMensaje("Usuario y contraseņa no coinciden");
				result.setStatus(401);
				result.setUsuario(new Persona());
				return result;
				
			}
		} catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public boolean existeUsuario(String email){

		try{
			String query = "SELECT * FROM USERS " +
					"WHERE email='"+ email +"'";
			System.out.println(query);
			ResultSet resultSet = sql.getResultset(query);

			if(resultSet.next()){
				System.out.println("Existe el usuario");
				return true;
			}
			else{
				System.out.println("usuario no existe");
				return false;
				
			}
		} catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
	}
	
	

	public SimpleToken registerUser(String email, String password, String usuario, String nombre, String direccion, String telefono, int ciudad, String web, Double latitud, Double longitud ) {
		// TODO Auto-generated method stub
		if(existeUsuario(email)){
			SimpleToken result = new SimpleToken();
			result.setMensaje("El email ya existe");
			result.setStatus(400);
		//mandar token diciendo q no se pudo registrar usuario
			return result;
		}
		else{
		
		String query = "INSERT INTO USERS(role_id,username,password,name,email,address,phone," +
				"location_id,website,latitude, longitude) " +
				"VALUES(2,'"+usuario+"','"+password+"'," +
				"'"+nombre+"','"+email+"'," +
				"'"+direccion+"','"+telefono+"'," +
				"'"+ciudad+"','"+web+"',"+
				" "+latitud+","+" "+longitud+")";
		sql.executeQuery(query);
		SimpleToken result = new SimpleToken();
		result.setMensaje("Usuario creado correctamente");
		result.setStatus(200);
		
		return result;
		}
	}
	
	public SimpleToken addPackage(int cantidad, Integer idPadre, int idResource) {
		String query;
		if (idPadre != null){
			query = "INSERT INTO PACKAGE(cantidad,id_package_padre,id_resource) " +
					"VALUES('"+cantidad+"','"+idPadre.intValue()+"','"+idResource+"')";
		}else {
			query = "INSERT INTO PACKAGE(cantidad,id_resource,enUso) " +
					"VALUES('"+cantidad+"','"+idResource+"','0')";
		}
			
		sql.executeQuery(query);
		SimpleToken result = new SimpleToken();
		result.setMensaje("paquete creado correctamente");
		result.setStatus(200);
		
		return result;

	}
	
	public SimpleToken sendPackage(int cantidad, Integer idPadre, int idResource) {
		// TODO Auto-generated method stub
		String query;
		if (idPadre != null){
			query = "INSERT INTO PACKAGE(cantidad,id_package_padre,id_resource) " +
					"VALUES('"+cantidad+"','"+idPadre.intValue()+"','"+idResource+"')";
		}else {
			query = "INSERT INTO PACKAGE(cantidad,id_resource) " +
					"VALUES('"+cantidad+"','"+idResource+"')";
		}
			
		sql.executeQuery(query);
		SimpleToken result = new SimpleToken();
		result.setMensaje("paquete enviado correctamente");
		result.setStatus(200);
		
		return result;

	}
}