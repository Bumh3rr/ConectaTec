package com.conectatec.ui.auth;

import android.app.Activity;
import android.content.Context;

import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.core.content.ContextCompat;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

public class GoogleAuthHelper {

    /** Resultado de la autenticación con Google. */
    public static class GoogleCredencial {
        public final String idToken;
        /** Email de la cuenta Google seleccionada por el usuario. */
        public final String email;

        public GoogleCredencial(String idToken, String email) {
            this.idToken = idToken;
            this.email   = email;
        }
    }

    /** Callbacks para el resultado de la autenticación. Todos se llaman en el hilo principal. */
    public interface Callback {
        void onExito(GoogleCredencial credencial);
        void onCancelado();
        void onError(String mensaje);
    }

    private final CredentialManager credentialManager;
    private final String webClientId;

    public GoogleAuthHelper(Context context, String webClientId) {
        this.credentialManager = CredentialManager.create(context);
        this.webClientId       = webClientId;
    }

    /**
     * Lanza el selector nativo de cuentas de Google.
     * Debe llamarse desde el hilo principal (la Activity).
     * El resultado llega en el Callback, también en el hilo principal.
     */
    public void solicitarCredencial(Activity activity, Callback callback) {
        GetGoogleIdOption option = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build();

        credentialManager.getCredentialAsync(
                activity,
                request,
                null,
                ContextCompat.getMainExecutor(activity),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        try {
                            GoogleIdTokenCredential googleCred =
                                    GoogleIdTokenCredential.createFrom(
                                            result.getCredential().getData());
                            callback.onExito(new GoogleCredencial(
                                    googleCred.getIdToken(),
                                    googleCred.getId()));
                        } catch (Exception e) {
                            callback.onError(e.getMessage() != null
                                    ? e.getMessage() : "Error al leer credencial");
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        if (e instanceof GetCredentialCancellationException) {
                            callback.onCancelado();
                        } else {
                            callback.onError(e.getMessage() != null
                                    ? e.getMessage() : "Error de autenticación");
                        }
                    }
                });
    }
}
