package com.bisontecfacturacion.security.auth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.bisontecfacturacion.security.model.ControlUsuario;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.ControlUsuarioRepository;
import com.bisontecfacturacion.security.service.IUsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer{
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private ControlUsuarioRepository controlUsuarioReporitory;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Map<String, Object> info = new HashMap<>();
		info.put("id", usuario.getId());
		info.put("f", usuario.getFuncionario().getId());
        info.put("username", usuario.getUsername());
        info.put("a", usuario.getAdministrador());
        info.put("p", usuario.getFuncionario().getPersona().getNombre() + " " + usuario.getFuncionario().getPersona().getApellido());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		ControlUsuario entity=new ControlUsuario();
		entity.getUsuario().setId(usuario.getId());
		entity.setFecha(new Date());
		entity.setHora(hora());
		controlUsuarioReporitory.save(entity);
		
		return accessToken;
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

}