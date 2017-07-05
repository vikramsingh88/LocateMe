package com.vikram.locateme.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountUtils {

	public static final String ACCOUNT_TYPE = "com.vikram.locateme";
	public static final String AUTH_TOKEN_TYPE = "com.vikram.locateme.authtoken.type";
	
	public static Account getAccount(Context context, String accountName) {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
		for (Account account : accounts) {
			if (account.name.equalsIgnoreCase(accountName)) {
				return account;
			}
		}
		return null;
	}
	
}
