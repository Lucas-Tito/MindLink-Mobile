package com.titolucas.mindlink.agenda.data;

import java.util.List;

public class AgendaResult {
    private final List<String> agendaItems;

    public AgendaResult(List<String> agendaItems) {
        this.agendaItems = agendaItems;
    }

    public List<String> getAgendaItems() {
        return agendaItems;
    }
}
