package com.adda.mistry.booking.addamistrybookingservice.exceptions;

public class BookingNotFoundException extends Exception{
    private static final long serialVersionUID = -470180507998010368L;

    public BookingNotFoundException() {
        super();
    }

    public BookingNotFoundException(final String message) {
        super(message);
    }
}
