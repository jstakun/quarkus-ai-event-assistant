package pl.redhat;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import jakarta.enterprise.context.ApplicationScoped;
import pl.redhat.geo.GeoCodingRestService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

@RegisterAiService 
@ApplicationScoped
@SystemMessage("""
        Jesteś inteligentnym asystentem. Użytkownicy będą zadawać Ci pytania dotyczące wydarzeń podając lokalizację, typ lub datę.
        Twoim zadaniem jest udzielanie dokładnych i zwięzłych odpowiedzi. 
        Wydarzenia mają różny typ taki jak sport, technologia, kultura, muzyka, biznes, rozrywka czy jedzenie.
        Jeżeli data wydarzenia została podana to zawsze zamień ją na format YYYY-MM-DD
        Masz dostęp do zbioru narzędzi.
        Możesz używać wielu narzędzi jednocześnie.
        Uzupełnij odpowiedź, korzystając z danych uzyskanych z narzędzi.
        W odpowiedzi podaj nazwę miasta, nazwę wydarzenia, opis wydarzenia i datę wydarzenia.
        Jeśli nie możesz uzyskać dostępu do narzędzi, aby odpowiedzieć na pytanie użytkownika,
        powiedz użytkownikowi, że żądane informacje nie są obecnie dostępne i że może spróbować ponownie później.
""")
public interface EventAssistant {
    
    @UserMessage("""
        {message}
            """)
    @ToolBox({EventService.class, GeoCodingRestService.class})
    public String assistUser(String message);
}
