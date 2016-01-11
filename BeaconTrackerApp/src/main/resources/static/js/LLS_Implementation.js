function lls_positioning(anchors, distances) {
	
	function sumColumn(column) {
		
	}

	//console.log(anchors);
	//console.log(distances);
	var dimensions = anchors.dimensions();
	var sumX = 0, sumY = 0, sumXSquared = 0, sumYSquared = 0, sumDSquared = 0;
	
	anchors.col(1).map(function(x) {
		sumX += x;
		return x;	
	});
	sumX = sumX/dimensions.rows;
	
	anchors.col(1).map(function(x) {
		sumXSquared += x*x;
		return x;	
	});
	sumXSquared = sumXSquared/dimensions.rows;
	
	anchors.col(2).map(function(y) {
		sumY += y;
		return y;	
	});
	sumY = sumY/dimensions.rows;
	
	anchors.col(2).map(function(y) {
		sumYSquared += y*y;
		return y;	
	});
	sumYSquared = sumYSquared/dimensions.rows;
	
	distances.map(function(d){
		sumDSquared += d*d;
		return d;
	})
	sumDSquared = sumDSquared/distances.dimensions();
	//console.log(sumDSquared);
	var elements = [];
	for(i = 0; i < dimensions.rows; i++) {
		elements[i] = [];
		elements[i][0] = anchors.e(i+1, 1) - sumX;
		elements[i][1] = anchors.e(i+1, 2) - sumY;
	}
	var A = Matrix.create(elements);
	var At = A.transpose();
	var Ainv = At.multiply(A).inv().multiply(At);
	elements = [];
	for(i = 0; i < dimensions.rows; i++) {
		elements[i] = [];
		elements[i][0] = anchors.e(i+1, 1) * anchors.e(i+1, 1) - sumXSquared;
		elements[i][0] += anchors.e(i+1, 2) * anchors.e(i+1, 2) - sumYSquared;
		elements[i][0] -= distances.e(i+1) * distances.e(i+1) - sumDSquared;
		
	}
	var B = Matrix.create(elements);
	B = B.multiply(0.5);
	
	var posM = Ainv.multiply(B).transpose();
	pos = [];
	pos[0] = posM.e(1,1);
	pos[1] = posM.e(1,2);
	return pos;

}