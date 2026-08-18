package dev.iuredev.HelpDeskAPI.enums;

public enum Priority {

    BAIXA(72),
    MEDIA(48),
    ALTA(24),
    CRITICA(4);

    private final int slaHours;

    Priority(int slaHours) {
        this.slaHours = slaHours;
    }

    public int getSlaHours() {
        return slaHours;
    }

}
