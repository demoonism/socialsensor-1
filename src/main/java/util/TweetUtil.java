package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zahraiman on 9/29/15.
 */
public class TweetUtil {


    public List<String> getGroupHashtagList(int groupNum, boolean localRun) {
        List<String> hashtagList = new ArrayList<>();
        if(localRun){
            hashtagList.add("h1");
            hashtagList.add("h5");
            hashtagList.add("h9");
        }else {
            String hashtagStrList = "";
            if (groupNum == 1)
                hashtagStrList = "ebola,prayforsouthkorea,haiyan,prayforthephilippines,ukstorm,pray4philippines,yycflood,abflood,mers,ebolaresponse,ebolaoutbreak,kashmirfloods,icestorm2013,h7n9,coflood,typhoon,boulderflood,typhoonhaiyan,dengue,serbiafloods,bayareastorm,hagupit,hellastorm,ukfloods,chikungunya,rabies,bosniafloods,h1n1,birdflu,chileearthquake,artornado,napaearthquake,evd68,arkansastornado";
            else if (groupNum == 2) // POLITICS
                hashtagStrList = "gazaunderattack,sotu,mh17,bringbackourgirls,snowden,blacklivesmatter,fergusondecision,gunsense,crimea,peshawarattack,election2014,governmentshutdown,netneutrality,standwithrand,hobbylobby,popefrancis,standwithwendy,defundobamacare,illridewithyou,bringbackourmarine,irantalks,newnjgunlaws,gopshutdown,prop8,fergusonoctober,occupycentral,navyyardshooting,bridgegate,israelunderfire,dearpopefrancis,bbcindyref,daesh,11mayprotests,prayformh17,ottawashooting,gopout,stopmarriagebill,occupyhk,stopthegop,votegunsense,newpope,shawshooting,umbrellarevolution,gazaunderfire,irantalksvienna,obamacareinthreewords,floodwallstreet,relegalize,1988iranmassacre,nonucleariran,midtermelections,europeanelections,afghanelections,ceasefirenow,occupygezy";
            else if (groupNum == 3) // GENERIC (1044 REDA HASHTAGS)
                hashtagStrList = "1000daysof1d,100cancionesfavoritas,100happydays,10thingsimattractedto,10thingsyouhatetodo,11million,14daysoffifa,1bigannouncement,1dalbumfour,1dalbumfourfollowspree,1dbigannouncement,1dconcertfilmdvd,1dday,1ddayfollowparty,1ddayfollowspree,1ddaylive,1defervescenciaccfm,1dfireproof,1dfragrance,1dmoviechat,1dmovieff,1dmoviepremiere,1dorlando,1dproposal,1dthisisus,1dthisisusff,1dtoday,1dtuesdaytest,1dwwafilm,1dwwafilmtrailer,2013follow,2013taughtme,2014in5words,2014mama,20cancionesfavoritas,20grandesquemellevodel2013,20personasimportantesenmivida,22personasespeciales,24lad,24seven,25m,3000miles,3daysforexpelledmovie,3daystill5hboss,3yearsofonedirection,40principales1d,42millionbeliebers,44millionbeliebers,44millionbeliebersfollowparty,4musiclfsbeliebers,4musiclfsdirectioners,4musiclfslittlemonsters,4thofjuly,4yearsago5strangersbecame5brothers,4yearsof1d,4yearsof1dfollowspree,4yearsofonedirection,5countries5days,5daysforexpelledmovie,5hcountdowntochristmas,5millionmahomies,5moresecondsofsummer,5sosalbumfollowspree,5sosderpcon,5sosdontstopfollowme,5sosdontstopfollowspree,5sosfambigvote,5sosfamilyfollowspree,5sosgot2millionfollowersfollowparty,5sosupindisstream,5sosvotinghour,5wordsihatetohear,69factsaboutme,6thfan,7millionmahomies,7yearsofkidrauhl,8daysforexpelledmovie,aaandrea,aadian,aaliyahmovie,aaronsfirstcover,aaronto1m,aaronto600k,aaronto700k,aaronto800k,aatwvideo,accela,actonclimate,actonreform,addawordruinamovie,advancedwarfare,aeronow,afazenda,aga3,agentsofshield,ahscoven,ahsfreakshow,albert_stanlie,alcal100k,alcs,alexfromtarget,alfredo1000,aliadosmusical,alitellsall,allday1ddayfollowspree,allieverneed,allonabilla,allthatmattersmusicvideo,allyfollowme,allylovesyou,alsicebucketchallenge,altband,alwayssupportluhan,amas2014,amazoncart,americanproblemsnight,andreganteng,angelatorres,anotherfollowtrain,anthonytuckerca,applausevid819,applelive,applepay,aprilwishes,areyoutheone,arianagrandeptw,arianalovesyou,ariananow,arianatorshelparianators,artistoftheyearhma,artrave,ash5sosfollowme,askarianagrande,askkendallschmidt,askmiley,asknash,asktyleranything,austinto6m,austinto6million,automaticturnons,azadimarchpti,azadisquare,bail4bapuji,bailon7thjan,bamforxmas,bamshiningstar,bangabanga,bangerztour,bap1004,bapuji,batalla_alboran,batimganteng,bb16,bb8,bbathechase,bbcan2,bbhotshots,bblf,bbma,bbmas,bbmzansi,beafanday,believeacousticforgrammy,believemovieposter,believemovieweekend,believepremiere,bellletstaik,bestcollaboration,bestfandom,bestfandom2014,bestplacetolistentobestsongever,bestsongevermusicvideotodayfollowparty,betawards2014,bethanymotacollection,bethanymotagiveaway,bieberchristmas,blacklivesmatter,bluemoontourenchile,bostonstrong,brager,bravsger,brazilvsgermany,breall,brelandsgiveaway,brentrivera,brentto300k,bringbackourgirls,bringbackourmarine,britishband,britsonedirection,buissness,bundyranch,buybooksilentsinners,buyproblemonitunes,bythewaybuytheway,cabletvactress,cabletvdrama,caiimecam,cailmecam,calimecam,callmesteven,cameronmustgo,camfollowme,camilacafollowspree,camilachameleonfollowspree,camilalovesfries,camilasayshi,camsbookclub,camsnewvideo,camsupdatevideo,camto1mill,camto2mil,camto2million,camto4mil,camto4mill,camwebstartca,candiru_v2,caoru,caraquici,carinazampini,cartahto1mil,carterfollowme,carterfollowspree,cartersnewvideo,carterto1mil,carterto1million,carterto300k,cashdash,cashnewvideo,cdm2014,ces2014,cfclive,changedecopine,childhoodconfessionnight,christmasday,christmasrocks,closeupforeversummer,clublacura,codghosts,colorssavemadhubalaeiej,comedicmovieactress,comedictvactor,comedictvactress,cometlanding,comiczeroes,conceptfollowspree,confessyourunpopularopinion,confidentvideo,congrats5sos,connorhit2million,connorto800k,contestentry,copyfollowers,cr4u,crazymofofollowspree,crazymofosfollowparty,crazymofosfollowspree,criaturaemocional,crimea,crimingwhilewhite,csrclassics,daretozlatan,darrenwilson,davidables,dday70,defundobamacare,del40al1directionersdream,demandavote,demiversary,demiworldtour,dhanidiblockricajkt48,dianafollowparty,didntgetaniallfollowfollowparty,directionermix1065,directionersandbeliebersfollowparty,directvcopamundial,disneymarvelaabcd,djkingassassin,dodeontti,dogecoin,donaldsterling,donetsk,dontstop5sos,dontstopmusicvideo,drqadri,drunkfilms,dunntrial,e32014,educadoresconlahabilitante,educatingyorkshire,elipalacios,emaazing,emabigge,emabiggestfan,emabiggestfans,emabiggestfans1d,emabiggestfans1dᅠ,emabiggestfans5sos,emabiggestfansarianagrande,emabiggestfansj,emabiggestfansju,emabiggestfansjustinbieber,encorejkt48missionst7,entrechavistasnosseguimos,epicmobclothing,ericgarner,ericsogard,esimposiblevivirsin,esurancesave30,etsymnt,euromaidan,eurovisionsongcontest2014,eurovisiontve,everybodybuytheway,exabeliebers,exadirectioners,exarushers,expelledmovie,expelledmovietonumberone,experienciaantiplan,facetimemecam,factsonly,fairtrial4bapuji,fake10factsaboutme,fakecases,fallontonight,fanarmy,fandommemories2013,farewellcaptain,fatalfinale,faze5,fergusondecision,fetusonedirectionday,ffmebellathorne,fictionalcharactersiwanttomarry,fictionaldeathsiwillnevergetover,fifa15,filmfridays,finallya5sosalbum,finalride,findalice,findingcarter,folllowmecam,follobackinstantly,follow4followed,followcarter,followella,followerscentral,followeverythinglibra,followliltwist,followmeaustincarlile,followmebefore2014,followmebrent,followmecarter,followmecon,followmeconnor,followmehayes,followmejack,followmejg,followmejoshujworld,followmelittlemixstudio,followmenash,followmeshawn,followmetaylor,followpyramid,followtrick,fordrinkersonly,fourhangout,fourtakeover,freebiomass,freejustina,freenabilla,freethe7,funnyonedirectionmemories,funwithhashtag,fwenvivoawards,gabesingin,gamergate,geminisweare,georgeujworld,gerarg,gervsarg,getbossonitunes,getcamto2mil,getcamto2million,getcamto3mil,getcamto3mill,getcamto800k,getcovered,getsomethingbignov7,gha,gigatowndun,gigatowndunedin,gigatowngis,gigatownnsn,gigatowntim,gigatownwanaka,givebackphilippines,gleealong,globalartisthma,gmff,gobetter,gobiernodecalle,goharshahi,gonawazgo,goodbye2013victoria,got7,got7comeback,gotcaketour2014,governmentshutdown,gpawteefey,gpettoe_is_a_scammer,grandtheftautomemories,greenwall,gtaonline,h1ddeninspotify,h1ddeninspotifydvd,haiyan,handmadehour,happybirthdaybeliebers,happybirthdaylouis,happybirthdayniall,happybirthdaytheo,happyvalentines1dfamily,harryappreciationday,hastasiemprecerati,havesandhavenots,hayesnewvideo,hayesto1m,hearthstone,heat5sos,heatjustinbieber,heatonedirection,heatplayoffs,heforshe,hermososeria,hey5sos,hiari,hicam,himymfinale,hiphopawards,hiphopsoty,hollywoodmusicawards,hometomama,hoowboowfollowtrain,hormonesseason2,houston_0998,hr15,hrderby,htgawm,iartg,icc4israel,icebucketchallenge,ifbgaintrain,igetannoyedwhenpeople,iheartawards,iheartmahone,illridewithyou,imeasilyannoyedby,immigrationaction,imniceuntil,imsotiredof,imsousedtohearing,imthattypeofpersonwho,imtiredofhearing,incomingfreshmanadvice,indiannews,inners,intense5sosfamattack,inthisgenerationpeople,ios8,iphone6plus,irememberigotintroublefor,isabellacastilloporsolista,isacastilloporartista,isil,italianmtvawards,itzbjj,iwanttix,iwishaug31st,jackto500k,jalenmcmillan,james900k,jamesfollow,jamesto1m,jamesyammounito1million,janoskianstour,jcfollowparty,jcto1million,jcto700k,jerhomies,jjujworld,joshujworld,justiceformikebrown,justinfollowalljustins,justinformmva,justinmeetanita,justwaitonit,kasabi,kaththi,kcaargentina,kcaᅠ,kcaméxico,kcamexico,kedsredtour,kellyfile,kikifollowspree,kingbach,kingofthrones,kingyammouni,knowthetruth,kobane,kykaalviturungunung,laborday,lacuriosidad,lastnightpreorder,latemperatura,leeroyhmmfollowparty,lesanges6,letr2jil,lhhatlreunion,lhhhollywood,liamappreciationday,libcrib,liesivetoldmyparents,lifewouldbealotbetterif,lindoseria,linesthatmustbeshouted,littlemixsundayspree,livesos,lollydance,longlivelongmire,lopopular2014,lorde,losdelsonido,louisappreciationday,lovemarriottrewards,mabf,macbarbie07giveaway,madeinaus,madisonfollowme,magconfollowparty,mahomiesgohardest,makedclisten,malaysiaairlines,maleartist,mama2013,mandelamemorial,mariobautista,marsocial,martinastoessel,maryamrajavi,mattto1mil,mattto1mill,mattto2mill,mattyfollowspree,mchg,meetthevamily,meetthevamps,meninisttwitter,mentionadislike,mentionpeopleyoureallylove,mentionsomeoneimportantforyou,mercedeslambre,merebearsbacktoschool,metrominitv,mgwv,mh17,mh370,michaelbrown,michaelisthebestest,midnightmemories,midnightmemoriesfollowparty,mileyformmva,mipreguntaes,miprimertweet,mis10confesiones,mis15debilidades,missfrance2014,missfrance2015,mixfmbrasil,mmlp2,mmva,monstermmorpg,monumentour,moremota,motafam,motatakeover,movieactress,movimentocountry,mplaces,mpointsholiday,mrpoints,mtvclash,mtvh,mtvho,mtvhot,mtvhott,mtvhotte,mtvhottes,mtvkickoff,mtvmovieawards,mtvs,mtvst,mtvsta,mtvstar,mtvsummerclash,mufclive,mufflerman,multiplayercomtr,murraryftw,murrayftw,musicjournals,my15favoritessongs,myboyfriendnotallowedto,myfourquestion,mygirlfriendnotallowedto,mynameiscamila,myxmusicawards,my_team_pxp,nabillainnocente,nairobisc,nakedandafraid,nash2tomil,nashsnewvid,nashsnewvideo,nashto1mill,nashto2mil,nblnabilavoto,neonlightstour,networktvcomedy,net_one,neversurrenderteam,newbethvideo,newsatquestions,newyearrocks,nhl15bergeron,nhl15duchene,nhl15oshie,nhl15subban,niallappreciationday,niallsnotes,nightchanges,nightchangesvideo,nionfriends,nj2as,no2rouhani,nominateaustinmahone,nominatecheryl,nominatefifthharmony,nominatethevamps,nonfollowback,noragrets,notaboyband,notersholiday2013,nothumanworld,noticemeboris,notyourshield,nowmillion,nowplayingjalenmcmillanuntil,nudimensionatami,nwts,o2lfollowparty,o2lhitamillion,occupygezi,officialsite,officialtfbjp,oitnb,onedirectionencocacolafm,onedirectionformmva,onedirectionptw,onedirectionradioparty,onemoredaysweeps,onenationoneteam,onsefollowledimanchesanspression,operationmakebiebersmile,oppositeworlds,opticgrind,orangeisthenewblack,orianasabatini,oscartrial,othertech,pablomartinez,pakvotes,parentsfavoriteline,paulachaves,paulafernandes,pcaforsupernatural,pdx911,peachesfollowtrain,pechinoexpress,peligrosincodificar,peopieschoice,peopleireallywanttomeet,peoplewhomademyyeargood,perduecrew,perfectonitunes,perfectoseria,peshawarattack,peterpanlive,playfreeway,pleasefollowmecarter,popefrancis,postureochicas,pradhanmantri,praisefox,prayforboston,prayformh370,prayforsouthkorea,prayforthephilippines,praytoendabortion,preordershawnep,pricelesssurprises,queronotvz,qz8501,randbartist,rapgod,raplikelilwayne,rbcames,rcl1milliongiveaway,rdmas,re2pect,realliampaynefollowparty,redbandsociety,relationshipgoals,rememberingcory,renewui,renhotels,replacemovietitleswithpope,retotelehit,retotelehitfinalonedirection,retweetback,retweetfollowtrain,retweetsf,retweetsfo,retweetsfollowtrain,rickychat,ripcorymonteith,riplarryshippers,ripnelsonmandela,rippaulwalker,riprobinwilliams,riptalia,ritz2,rmlive,rollersmusicawards,sammywilk,sammywilkfollowspree,samwilkinson,savedallas,sbelomusic,sbseurovision,scandai,scandalfinale,scarystoriesin5words,scifiactor,scifiactress,scifitv,selenaformmva,selenaneolaunch,selffact,setting4success,sexylist2014,sh0wcase,shareacokewithcam,sharknado,sharknado2,sharknado2thesecondone,shawnfollowme,shawnsfirstsingle,shawnto1mil,shawnto500k,shelookssoperfect,sherlocklives,shotsto600k,shotties,shouldntcomeback,simikepo,simplementetini,skipto1mill,skywire,smoovefollowtrain,smpn12yknilaiuntertinggi,smpn12yksuksesun,smurfsvillage,smurfvillage,sobatindonesia,socialreup,somebodytobrad,somethingbigatmidnight,somethingbigishappening,somethingbigishappeningnov10,somethingbigtonumber1,somethingbigvideo,somethingthatwerenot,sometimesiwishthat,sonic1d,sosvenezuela,soydirectioner,spamansel,spinnrtaylorswift,sportupdate,spreadcam,spreadingholidaycheerwithmere,ss8,ssnhq,standwithrand,standwithwendy,starcrossed,staystrongexo,stealmygiri,stealmygirl,stealmygirlvevorecord,stealmygirlvideo,stilababe09giveaway,storm4arturo,storyofmylife16secclip,storyofmylifefollowparty,storyofmylifefollowspree,summerbreaktour,superjuniorthelastmanstanding,supernaturai,sydneysiege,takeoffjustlogo,talklikeyourmom,talktomematt,tampannbertanya,taylorto1m,taylorto1mill,taylorto58285k,taylorto900k,tayto1,tcas2014,tcfollowtrain,teamfairyrose,teamfollowparty,teamfree,teamgai,teamrude,techtongue,teenawardsbrasil,teenchoice,telethon7,tfb_cats,thanksfor3fantasticyears1d,thankyou1d,thankyou1dfor,thankyoujesusfor,thankyouonedirectionfor,thankyousachin,thankyousiralex,that1dfridayfeelingfollowspree,thatpower,the100,thebiggestlies,theconjuring,thefosterschat,thegainsystem,thegifted,thehashtagslingingslasher,themonster,themostannoyingthingsever,thepointlessbook,thereisadifferencebetween,thesecretonitunes,thevamps2014,thevampsatmidnight,thevampsfollowspree,thevampssaythanks,thevampswildheart,theyretheone,thingsisayinschoolthemost,thingsiwillteachmychild,thingspeopledothatpissmeoff,thisisusfollowparty,thisisusfollowspree,threewordsshewantstohear,threewordstoliveby,tiannafollowtrain,time100,timepoy,tinhouse,tipsfornewdirectioners,tipsforyear7s,titanfall,tityfolllowtrain,tixwish,tns7,tntweeters,tokiohotelfollowspree,tomyfuturepartner,tonyfollowtrain,topfiveunsigned,topfollow,topfollowback,topretw,topretweet,topretweetgaintra,topretweetgaintrai,topretweetgaintrain,topretweetmax,toptr3ce,torturereport,totaldivas,toydefense2,tracerequest,trevormoranto500k,trndnl,truedetective,ts1989,tvbromance,tvcrimedrama,tvgalpals,tvtag,tweeliketheoppositegender,tweetlikejadensmith,tweetliketheoppositegender,tweetmecam,tweetsinchan,twitterfuckedupfollowparty,twitterpurge,uclfinal,ufc168,ultralive,unionjfollowmespree,unionjjoshfollowspree,unionjustthebeginning,unitedxxvi,unpopularopinionnight,uptime24,usaheadlines,user_indonesia,utexaspinkparty,vampsatmidnight,verifyaaroncarpenter,verifyhayesgrier,vevorecord,vincicartel,vinfollowtrain,visitthevamps,vmas2013,voiceresults,voicesave,voicesaveryan,vote4austin,vote4none,vote5hvma,vote5hvmas,vote5os,vote5sosvmas,voteaugust,votebilbo,voteblue,votecherlloyd,votedemilovato,votedeniserocha,votefreddie,votegrich,votejakemiller,votejennette,votejup,votekatniss,votekluber,voteloki,voteluan,votematttca,votemiley,votemorneau,voterizzo,votesamandcat,votesnowwhite,votesuperfruit,votetimberlake,votetris,votetroyesivan,voteukarianators,voteukdirectioners,voteukmahomies,votevampsvevo,voteveronicamars,votezendaya,waliansupit,weakfor,weareacmilan,weareallharry,weareallliam,weareallniall,weareallzayn,wearewinter,webeliveinyoukris,wecantbeinarelationshipif,wecantstop,welcometomyschoolwhere,weloveyouliam,wethenorth,wewantotrainitaly2015,wewantotratourinitaly2015,wewillalwayssupportyoujustin,whatidowheniamalone,wheredobrokenheartsgo,whereismike,wherewearetour,whitebballpains,whitepeopleactivities,whowearebook,whybeinarelationshipif,whyimvotingukip,wicenganteng,wildlifemusicvideo,wimbledon2014,win97,wmaexo,wmajustinbieber,wmaonedirection,womaninbiz,wordsonitunestonight,worldcupfinal,worldwara,wwadvdwatchparty,wwat,wwatour,wwatourfrancefollowspree,xboxone,xboxreveal,xf7,xf8,xfactor2014,xfactorfinal,yammouni,yeremiito21,yesallwomen,yespimpmysummerball,yespimpmysummerballkent,yespimpmysummerballteesside,yolandaph,youmakemefeelbetter,younusalgohar,yourstrulyfollowparty,ytma,z100jingleball,z100mendesmadness,zaynappreciationday,zimmermantrial,__tfbjp_";
            Collections.addAll(hashtagList, hashtagStrList.split(","));
        }
        return hashtagList ;
    }
}
