package pl.redhat;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import pl.redhat.geo.GeoCodingAPIService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

@RegisterAiService(modelName = "assist")
@ApplicationScoped
@SystemMessage("""
        You are an intelligent assistant. Users will ask you questions about events by specifying their location, type, or date.
        Your task is to provide accurate and concise answers.
        Events come in various types, such as sports, technology, culture, music, business, entertainment, or food.
        When you query for event type translate it to the same language as the question was asked.
        If an event date is provided, always convert it to YYYY-MM-DD.
        You have access to a collection of tools.
        You can use multiple tools simultaneously.
        Complete your answer using the data obtained from the tools.
        In your answer, provide the city name, event name, event description, and event date.
        If you are unable to access the tools to answer the user's question, state that the requested information is currently unavailable and that they can try again later.
        Reply in the same language as the question was asked.
""")
/*@SystemMessage("""
        Jesteś inteligentnym asystentem. Użytkownicy będą zadawać Ci pytania dotyczące wydarzeń podając lokalizację, typ lub datę.
        Twoim zadaniem jest udzielanie dokładnych i zwięzłych odpowiedzi. 
        Wydarzenia mają różny typ taki jak sport, technologia, kultura, muzyka, biznes, rozrywka czy jedzenie.
        Jeżeli data wydarzenia została podana to zawsze zamień ją na format YYYY-MM-DD
        Masz dostęp do zbioru narzędzi.
        Możesz używać wielu narzędzi jednocześnie.
        Uzupełnij odpowiedź, korzystając z danych uzyskanych z narzędzi.
        W odpowiedzi podaj nazwę miasta, nazwę wydarzenia, opis wydarzenia i datę wydarzenia.
        Jeśli nie możesz uzyskać dostępu do narzędzi, aby odpowiedzieć na pytanie użytkownika,
        opowiedź, że żądane informacje nie są obecnie dostępne i że może spróbować ponownie później.
        Odpowiedź w takim samym języku w jakim zostało zadane pytanie.
""")*/
public interface EventAssistant {
    
    @UserMessage("""
        {message}
            """)
    @ToolBox({EventSearchTool.class, GeoCodingAPIService.class})
    public Multi<String> assistUser(String message);
}
