# PfWHelper
0. Potrzebna jest wersja java w wersji 12 - do pobrania, np. tutaj: https://jdk.java.net/java-se-ri/12

1. Uruchamianie:
java -jar PfWHelper-1.0-SNAPSHOT.jar <<root_path>>
np. java -jar PfWHelper-1.0-SNAPSHOT.jar "D:\git-repo\IslamicBank\PfWHelper\example\cif"

2. Odczytywanie wyników:
Program zwraca różne rzeczy na samym początku - proszę zignorować. Raport zaczyna się w miejscu:
UUID: bdc9fdeb-ead2-40fd-8591-96b5c7777607
Date: 02 czerwiec 2022
Root Path: D:\git-repo\IslamicBank\PfWHelper\example\cif

2.1 Każdy z plików (a w zasadzie nazw plików z*.srd) jest analizowany w osobnej sekcji.
Najważniejsze części to:
- No. of matches wskazuje czy udało się znaleźć pełne powiązanie wg algorytmu
- File: nazwa pliku *srd
- każde zagłębienie to jeden krok w głąb według algorytmu
	- każde zagłębienie składa się z nazwy pliku sru, który był analizowany oraz czego szukano
	- w przypadku znalezienia powiązań na samym dole - najgłębiej - będzie lista odnalezionych plików

2.2 Przykład 1
File: D:\git-repo\IslamicBank\PfWHelper\example\cif\20_cif_dw\zd_cifinq_defrecord_detail.srd
No. of matches: 0
Status: found related *.sru file(s); could not find uo_inquirymaintain.sru file
        D:\git-repo\IslamicBank\PfWHelper\example\cif\20_cif\zuo_cifinq_defrecord_detail.sru
        string dataobject = "zd_cifinq_defrecord_detail"
        zd_cifinq_defrecord_detail
                        D:\git-repo\IslamicBank\PfWHelper\example\cif\20_cif\zuo_cifinq_defrecord_detail.sru
                        global type zuo_cifinq_defrecord_detail from uo_inquirymaintain
                        uo_inquirymaintain
[1. znaleziono plik sru z zd_cifinq_defrecord_detail, a wpliku referencję na uo_inquirymaintain]
[2. nie znaleziono pliku uo_inquirymaintain.sru]

2.3 Przykład 2
File: D:\git-repo\IslamicBank\PfWHelper\example\cif\z_nbp_customer_dw\zd_61_create_cif_commercial.srd
No. of matches: 2
Status: found related *.sru file(s)
        D:\git-repo\IslamicBank\PfWHelper\example\cif\z_nbp_customer_uo\zuo_61_create_cif_corporate.sru
        string dataobject = "zd_61_create_cif_commercial"
        zd_61_create_cif_commercial
                        D:\git-repo\IslamicBank\PfWHelper\example\cif\z_nbp_customer_uo\zuo_61_create_cif_corporate.sru
                        global type zuo_61_create_cif_corporate from uo_61_create_cif_corporate
                        uo_61_create_cif_corporate
                                        D:\git-repo\IslamicBank\PfWHelper\example\cif\61_cif\uo_61_create_cif_corporate.sru
                                        string dataobject = "d_61_create_cif_commercial"
                                        d_61_create_cif_commercial
                                                D:\git-repo\IslamicBank\PfWHelper\example\cif\61_cif_dw\d_61_create_cif_commercial.srd
                                                D:\git-repo\IslamicBank\PfWHelper\example\cif\63_cif_dw\d_61_create_cif_commercial.srd
[Pełen sukces, odnaleziono dwa pliki srd powiązane z kastomizowanym]

2.4 Przykład 3
File: D:\git-repo\IslamicBank\PfWHelper\example\cif\z_nbp_customer_dw\zd_dddw_zutbltrntype.srd
No. of matches: 0
Status: no related *.sru file found

[nie odnaleziono wystąpienia zd_dddw_zutbltrntype w żadnym z dostępnych plików *.sru]

3. Inne: program działa na 4 wątkach w części odczytującej pliki. Jeżeli okaże się to za wolno to są jeszcze możliwości do ulepszeń