package com.conectatec.ui.common;

public abstract class UiState<T> {

    private UiState() {}

    public static final class Loading<T> extends UiState<T> {
        private static final Loading<?> INSTANCE = new Loading<>();

        private Loading() {}

        @SuppressWarnings("unchecked")
        public static <T> Loading<T> get() {
            return (Loading<T>) INSTANCE;
        }
    }

    public static final class Success<T> extends UiState<T> {
        public final T data;

        public Success(T data) {
            this.data = data;
        }
    }

    public static final class Error<T> extends UiState<T> {
        public final String mensaje;

        public Error(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
