package com.skula.cpb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skula.cpb.services.DatabaseService;


public class ParamActivity extends Activity {
	private EditText transIp;
	private EditText transPort;
	private EditText transLogin;
	private EditText transPw;
	
	private EditText betaLogin;
	private EditText betaPw;
	private EditText betaKey;
	
	private DatabaseService dbService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.param_layout);
	
		this.dbService = new DatabaseService(this);
		dbService.bouchon();
		
		this.transIp = (EditText) findViewById(R.id.trans_ip);
		this.transPort = (EditText) findViewById(R.id.trans_port);
		this.transLogin = (EditText) findViewById(R.id.trans_login);
		this.transPw = (EditText) findViewById(R.id.trans_pw);
		
		this.betaLogin = (EditText) findViewById(R.id.beta_login);
		this.betaPw = (EditText) findViewById(R.id.beta_pw);
		this.betaKey = (EditText) findViewById(R.id.beta_key);
		
		Button btnSave = (Button)findViewById(R.id.btn_save);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				update();
			}
		});
		
		/*Button btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});*/
		
		fillFields();
	}
	
	private void update(){
		String strTransIp = transIp.getText().toString();
		String strTransPort = transPort.getText().toString();
		String strTransLogin = transLogin.getText().toString();
		String strTransPw = transPw.getText().toString();
		
		String strBetaLogin = betaLogin.getText().toString();
		String strBetaPw = betaPw.getText().toString();
		String strBetaKey = betaKey.getText().toString();
		
		if(strTransIp.isEmpty()){
			Toast.makeText(this,"L'IP pour Transmission est vide",Toast.LENGTH_SHORT);
			return;
		}
		/*if(!strTransIp.match("\\d*")){
			Toast.makeToast(this,"L'IP pour Transmission est incorrect",Toast.Short);
			return;
		}*/
		if(strTransPort.isEmpty()){
			Toast.makeText(this,"Le port pour Transmission est vide",Toast.LENGTH_SHORT);
			return;
		}
		if(strTransLogin.isEmpty()){
			Toast.makeText(this,"Le login pour Transmission est vide",Toast.LENGTH_SHORT);
			return;
		}
		if(strTransPw.isEmpty()){
			Toast.makeText(this,"Le mot de passe pour Transmission est vide",Toast.LENGTH_SHORT);
			return;
		}
		
		if(strBetaLogin.isEmpty()){
			Toast.makeText(this,"Le login pour Betaseries est vide",Toast.LENGTH_SHORT);
			return;
		}
		if(strBetaPw.isEmpty()){
			Toast.makeText(this,"Le mot de passe pour Betaseries est vide",Toast.LENGTH_SHORT);
			return;
		}
		if(strBetaKey.isEmpty()){
			Toast.makeText(this,"La clé pour Betaseries est vide",Toast.LENGTH_SHORT);
			return;
		}
		
		dbService.updateParam(Cnst.PARAM_IP_TRANSMISSION, strTransIp);
		dbService.updateParam(Cnst.PARAM_PORT_TRANSMISSION, strTransPort);
		dbService.updateParam(Cnst.PARAM_LOGIN_TRANSMISSION, strTransLogin);
		dbService.updateParam(Cnst.PARAM_PW_TRANSMISSION, strTransPw);
		
		dbService.updateParam(Cnst.PARAM_LOGIN_BETASERIES, strBetaLogin);
		dbService.updateParam(Cnst.PARAM_PW_BETASERIES, strBetaPw);
		dbService.updateParam(Cnst.PARAM_KEY_BETASERIES, strBetaKey);
		this.finish();
	}

	private void fillFields() {
		this.transIp.setText(dbService.getParam(Cnst.PARAM_IP_TRANSMISSION));
		this.transPort.setText(dbService.getParam(Cnst.PARAM_PORT_TRANSMISSION));
		this.transLogin.setText(dbService.getParam(Cnst.PARAM_LOGIN_TRANSMISSION));
		this.transPw.setText(dbService.getParam(Cnst.PARAM_PW_TRANSMISSION));
		
		this.betaLogin.setText(dbService.getParam(Cnst.PARAM_LOGIN_BETASERIES));
		this.betaPw.setText(dbService.getParam(Cnst.PARAM_PW_TRANSMISSION));
		this.betaKey.setText(dbService.getParam(Cnst.PARAM_KEY_BETASERIES));
	}
}
