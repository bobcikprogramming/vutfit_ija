/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbobci03.sharedClasses;

/**
 * Třída slouží k uložení konstant.
 * @author Pavel Bobčík (xbobci03)
 */
public class Constants {
    /**
     * Konstana cesty k umístnění obrázku.
     */
    public static final String pathImg = "file:lib/img/";
    
    /**
     * Konstana cesty k umístnění mapy skladiště.
     */
    public static final String pathMap = "data/map/";
    
    /**
     * Konstana cesty k umístnění FXML souboru.
     */
    public static final String pathFxml = "/gui/FXMLDocument.fxml";
    
    /**
     * Konstana cesty k umístnění css souboru.
     */
    public static final String pathCss = "/gui/MyGUISetting.css";
    
    /**
     * Konstana šířky obrázku.
     */
    public static final int imgWidth = 45;
    
    /**
     * Konstana výšky obrázku.
     */
    public static final int imgHeight = 45;
    
    /**
     * Konstana šířky skladiště.
     */
    public static final int mapWidth = 1125;
    
    /**
     * Konstana výšky skladiště.
     */
    public static final int mapHeight = 450;
    
    /**
     * Konstana souřadnice x pro počáteční uzel A*.
     */
    public static final int xBeg = 23;
    
    /**
     * Konstana souřadnice y pro počáteční uzel A*.
     */
    public static final int yBeg = 9;
            
    /**
     * Konstanta nesoucí obsah výpisu pro demonstrační odevzdání.
     */
    public static final String demo = "Dobrý den,\n"
            + "vítejte v ukázce demoverze (či postupu) naší aplikace.\n"
            + "Naši aplikaci jsme započali tvorbou GUI, kterému následně implementujeme funkcionalitu.\n"
            + "Níže uvedené řádky popisují co lze otestovat a dále jakým tlačítkem toho docílit.\n\n"
            + "Co zatím umí naše GUI:\n"
            + " - lze otevřít skladiště (v případě špatného formátu budete upozorněni),\n"
            + " - lze obnovit skladiště do původního stavu,\n"
            + " - lze si nechat vypsat obsah regálu,\n"
            + " - v případě prázdného regálu se zobrazí pozměnéná grafická reprezentace,\n"
            + " - lze přidat či odebrat překážku (+ výpis činosti),\n"
            + " - lze vyzamat výpis\n"
            + " - lze vypsat cestu pomocí A* (nutnost načtení skladiště), cesta je zatím staticky nastavena\n"
            + "     od počátku do bodu (1, 1), nicméně lze pomocí přidávání překážek cestu ovlivnit.\n\n"
            + "Skladiště otevřeme kliknutím na tlačítko 'Nahrát sklad' a následným výběrem ze souboru.\n"
            + "Cestu vypíšeme kliknutím na tlačítko 'Vykonat'."
            + "Obnovení provedeme tlačítkem 'Obnovit', nebylo-li skladiště nahráno, otevře se nám výběr.\n"
            + "Překážku lze přidat/odebrat kliknutím ne políčko cesty.\n"
            + "Výpis obsahu regálu získáme kliknutím na regál. Jeho obsah se zobrazí v tomto okně.\n"
            + "Toto okno lze vyčistit pomocí tlačítka 'Vymazat výpis'.\n\n";
    
    /**
     * Konstanta nesoucí výpis nápovědy.
     */
    public static final String help = "Nápověda k použití programu.\n"
            + "Příkazy k ovládání najdete dole nebo vypíšete pomocí příkazu 'prikazy.'.\n\n"
            + "Načtení skladiště:\n"
            + " - Načtení skladiště je základní požadavek, bez nějž není možné vykonávat úkol programu.\n"
            + " - Načtení lze provést pomocí tlačítka 'Nahrát sklad', jenž otevře okno pro výběr souboru.\n"
            + " - Provede se načtení regálů, volné políčka se nastaví jako cesta.\n"
            + " - Načtení podporuje formát .csv, který jako rozdělovače vyžaduje ';' (středník)\n"
            + "   a skládá se z x_souřadnice;y_souřadnice;název_zboží;počet.\n\n"
            + "Obnovení skladiště:\n"
            + " - Obnovení skladiště slouží k uvedení skladiště do původního stavu.\n"
            + " - Provedeme jej tlačítkem 'Obnovit'.\n"
            + " - V případě, že doposud nebylo žádné skladiště načteno, dojde k otevření okna pro výběr souboru.\n\n"
            + "Načtení požadavku (ze souboru):\n"
            + " - K načtení požadavku ze souboru slouží tlačítko 'Nahrát výdej', jenž otevře výběr souboru.\n"
            + " - Soubor má formální náležitosti:\n"
            + " +   Každý požadavek musí začínat slovem 'vydej:' následovaný seznamem k vydání,\n"
            + " +   kde je každé zboží na novém řádku. Korektní řádek se skládá z 'nazev, pocet'\n"
            + " +   a řádek je ukončen ',' (čárkou). V případě konce požadavku je řádek ukončen '.' (tečkou).\n"
            + " +   Jméno zboží nesmí obsahovat čárku. Příklad bude zobrazen níže nebo pomocí 'prikazy.'.\n\n"
            + "Vstupní konzole:\n"
            + " - Vstupní konzole umožňuje textový vstup uživatele a nachází se v levé dolní části programu.\n" 
            + " - Seznam příkazů lze vypsat pomocí příkazu 'prikazy.', nebo jej zobrazit v nápovědě.\n"
            + " - Dále slouží pro zadání požadavků na výdej ze skladu.\n"
            + " - K vykonání příkazu slouží tlačítko 'Vykonat'.\n\n"
            + "Výstupní konzole:\n"
            + " - Výstupní konzole slouží pro výpis dění aplikace.\n"
            + " - Mezi výpis patří:\n"
            + " +   Uzavření/otevření cesty.\n"
            + " +   Výpis nápovědy či příkazů.\n"
            + " +   Výpis obsahu skladu/regálu.\n"
            + " +   A další.\n\n"
            + "Smazat výpis:\n"
            + " - Tlačítko sloužící pro vyčištění výstupní konzole.\n\n"
            + "Ovládání času:\n"
            + " - Čas lze zpomalit/zrychlit pomocí tlačítek '/4 /2 x1 x2 x4'\n\n"
            + Constants.commands;
    
    /**
     * Konstanta nesoucí výpis příkazů.
     */
    public static final String commands = "Příkazy pro vstupní konzoli:\n"
            + " - Syntaxe:\n"
            + " +    Na jeden příkaz náleží právě jeden řádek, dva a více příkazů na řádku bude ignorováno.\n"
            + " +    Konec příkazu se značí '.' (tečkou).\n"
            + " +    Nezáleží na velikosti písma.\n\n"
            + " - vydej\n"
            + " +   Příkaz pro získání zadaného zboží ze skladu.\n"
            + " +   Syntaxe:\n"
            + "         vydej:\n"
            + "         zbozi, pocet,\n"
            + "         dalsi_zbozi, pocet.\n"
            + " +   Příklad:\n"
            + "         vydej:\n"
            + "         stul, 1,\n"
            + "         zidle, 4,\n"
            + "         prostirani, 1.\n"
            + " +   vydá ze skladu jeden stůl, čtyři židle a jedno prostírání.\n\n"
            + " - najdi\n"
            + " +   Příkaz pro nalezní daného zboží ve skladu. Vypíše jeho pozici, bylo-li nalezeno.\n"
            + " +   Syntaxe:\n"
            + "         najdi:nazev.\n"
            + " +   Příklad:\n"
            + "         najdi:stul.\n\n"
            + " - obsah\n"
            + " +   Příkaz, jenž vypíše obsah celého skladu.\n"
            + " +   Syntaxe:\n"
            + "         obsah.\n\n"
            + " - vymaz\n"
            + " +   Příkaz na vymazíní výstupní konzole. Například při uvedení před novým požadavkem na výdej.\n"
            + " +   Syntaxe:\n"
            + "         vymaz.\n\n"
            + " - prikazy\n"
            + " +   Příkaz pro zobrazení všech příkazů.\n"
            + " +   Syntaxe:\n"
            + "         prikazy.\n\n"
            + " - pomoc\n"
            + " +   Příkaz pro zobrazení nápovědy.\n"
            + " +   Syntaxe:\n"
            + "         pomoc.\n"; 
}
