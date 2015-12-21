function [x, out, ind] = removeOutliers(x)
% function removes outliers from a vector x based on inter quantile range
% (IQR) analysis
%
% INPUT:
%
% OUTPUT:
%   x   - vector of data WITHOUT outliers
%   out - vector of outliers
%   ind - indexes of outliers in input vector x
%

if length(x)<3
    out = [];
    ind = [];
    return;
end

% make sure input vector is a ROW vector
if ~isrow(x)
    x = x';
    conv2Col = 1; % keep to revert ROW to COLUMN vector at the end of a function
else
    conv2Col = 0;
end

data = x;
%% remove NaNs
ind = find(isnan(x)==1);
data(ind) = [];
%% sort the data
data = sort(data);

%% find mediane (middle)value
Q2 = median(data);

%% find lower and upper halves of data set 
len = length(data);
if mod(len,2)~=0 % vector length is ODD
    d_lower = data(1:(len-1)/2);
    d_upper = data((len+1)/2 + 1:end);
else
    d_lower = data(1:len/2);
    d_upper = data(len/2 + 1:end);
end

%% calculate quantiles Q1 and Q3
Q1 = median(d_lower);
Q3 = median(d_upper);
    
%% calculate InterQuantile Range (IQR)
IQR = 1.5*(Q3-Q1);
%% calculaye Upper and Lower Inner Fencies - UIF and LIF respectively
UIF = Q3+IQR;
LIF = Q1-IQR;

%% find indexes of outliers
ind = [ind find(x<LIF) find(x>UIF)];
out = x(ind);

%% remove values that fall outside fences
x = x(x>LIF);
x = x(x<UIF);

if conv2Col==1
    x = x';
    out = out';
    ind = ind';
end
    


