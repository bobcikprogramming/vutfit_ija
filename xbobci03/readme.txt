Název projektu: Simulátor skladiště
Název týmu: Tým xbobci03
Verze jazyka Java: Java 8
Členové: Pavel Bobčík (xbobci03), Tomáš Daněk (xdanek23)

Programová dokumentace se vygeneruje při kompilaci projektu. 

Kompilace projektu: ant compile
Zapnutí projektu: ant run

Při tvorbě projektu postupujeme od grafického rozhraní k funkcionalitě. Tudiž jsme si jako první vytvořili GUI, 
kterému postupně přidáváme jednotlivé funkce. GUI jsme se rozhodli vytvořit "old style". Tedy jsem se jednak vzhledem,
ale i ovládáním, zaměřili na nádech starší aplikace. Ovládání bude tedy, krom pár nutných tlačítek, probíhat pomocí
vstupní konzole. Nicméně víme, že běžný uživatel by nebyl nadšen ze psaní závorek a středníků, a tak je naším plánem
ovládání udělat více lidskou formou. Půjde o to, že jednotlivé pokyny budou připomínat věty. Na velikosti textu nezáleží,
ale každý příkaz je ukončen tečkou. Například výpis obsahu: 'Obsah.'

Pro hledání cesty jsme si vybrali algoritmus A*, jenž jsme oba mohli otestovat na projektu v základech umělých inteligencí.