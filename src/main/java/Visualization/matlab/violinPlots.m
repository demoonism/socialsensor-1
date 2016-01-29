fNames = { 'Hashtag' 'Location' 'Mention'};%'Term' 'From' 
plotInd = 1;
%ha = tight_subplot(2,5,[.06 .03],[.02 .04]);
for ifn = 1:length(fNames)
    fName = fNames{ifn};
    path = ['/scratch/PlotFiles/MI/' fName '/'];
    folders=dir(path);    
    for ij = 1:length(folders)
        folder = folders(ij);
        if (strcmp(folder.name(1),'.'))
            continue;
        end
        if(strcmp(folder.name,'statusesCount') || strcmp(folder.name, 'LocationDependent') )
           %strcmp(folder.name,'followersCount')||strcmp(folder.name,'favoriteCount') ||  || strcmp(folder.name,'friendsCount'))
            continue;
        end
        Files = dir([path folder.name '/']);
        q = zeros(6, 3*6);
        %figure;
        topics = [];
        ind = 1;topicInd = 1;
        for k=1:length(Files)
            fileNames=Files(k).name;
            fileNames
            if (strcmp(fileNames(1),'.') || strcmp(fileNames, 'BoxPlots') || strcmp(fileNames, 'Plots'))
               continue;
            end
            if(~strcmp(fileNames(1:min(length(fileNames),8)), 'irandeal') )
                continue;
            end
            topics{topicInd} = fileNames;        
            fid = fopen([path folder.name '/' fileNames]);
            out = textscan(fid,'%f%f%s%d','delimiter',',');
            counts = out{1};
            probs = out{2};
            out = [];
            len = length(probs);
            randIndices = randperm(len, min(len, 1e6));
            probs = probs(randIndices);
            counts = counts(randIndices);
            
            max(counts)
            %[N,edges] = histcounts(out{2},edges);
            edges = [1e2 1e3 1e4 1e5 1e6 1e8];
            edgesLabels = {'10e1', '10e2', '10e3', '10e4', '10e5', '10e7'};
            x = []; y = []; ind = 1;xl = [];tInd = 1;data= []; dataInd =1;
            facecolors = [1 1 0;0 1 0;1 0 1; 0 0 1; 0 1 1; .3 .3 .3];
            for i=1:length(edges)
                if i==1
                    P = probs(counts <= edges(i) & counts > 0);
                    x = [x; P];
                    y = [y; edges(i*ones(1, sum(counts <= edges(i) & counts > 0)))'];
                    pk = i*ones(1, sum(counts <= edges(i) & counts > 0));
                    if(length(P) > 0)
                        if(strcmp(fName, 'Term'))
                            data{dataInd} = log10(P+1e-16);
                        else
                            data{dataInd} = log10(P);
                        end
                        dataInd = dataInd+1;
                    end
                else
                    pk = i*ones(1, sum(counts <= edges(i) & counts > edges(i-1)));
                    P=probs(counts <= edges(i) & counts > edges(i-1));
                    x = [x; P];
                    y = [y; edges(pk)']; 
                    if(length(P) > 0)
                        if(strcmp(fName, 'Term'))
                            data{dataInd} = log10(P+1e-16);
                        else
                            data{dataInd} = log10(P);
                        end
                        dataInd = dataInd+1;
                    end
                end
                if(size(pk,2) > 0)
                    xl{tInd} = ['10e' num2str(pk(1))];
                    tInd = tInd+1;
                end                
                P = [];
                ind = ind+3;
            end
            figure; 
            violin(data, 'facecolor',facecolors(1:length(data), :))
            names = strsplit(fileNames, '_');
            names2 = strsplit(char(names(3)), '.');
            names3 = strsplit(char(names(1)), '-');
            titleName = [char(names3(2)), '-', char(names(2))];
            pName = [path,folder.name, '/ViolinPlots/',titleName];
            title(titleName,'FontWeight','bold');
           % xlabel(names(2));
           % ylabel('MI');
            
           set(gca,'xtick',1:length(data), 'xticklabel',edgesLabels(1:length(data)));
            pubmode('on');
            tightInset = get(gca, 'TightInset');
            position(1) = tightInset(1);
            position(2) = tightInset(2);
            position(3) = 1 - tightInset(1) - tightInset(3);
            position(4) = 1 - tightInset(2) - tightInset(4);
            set(gca, 'Position', position);
            exportfig(gcf, pName,'Format', 'eps', 'Color', 'rgb')
            export_fig(pName, '-pdf', '-transparent');
        end
    end
end
pubmode('on');
h = gcf;
tightInset = get(gca, 'TightInset');
position(1) = tightInset(1);
position(2) = tightInset(2);
position(3) = 1 - tightInset(1) - tightInset(3);
position(4) = 1 - tightInset(2) - tightInset(4);
set(gca, 'Position', position);
saveas(h, 'WithoutMargins.pdf');
iptsetpref('ImshowBorder','tight');
