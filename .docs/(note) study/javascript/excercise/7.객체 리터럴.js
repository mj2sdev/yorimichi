var obj = {};
var key = 'hello';

obj[key] = 'world';
//var obj = { [key]: 'world'};

console.log(obj); // {hello: 'world'}


var prefix = 'prop';
var i = 0;
var obj = {};

obj[prefix + '-' + ++i] = i;
obj[prefix + '-' + ++i] = i;
obj[prefix + '-' + ++i] = i;

console.log(obj); //{prop-1: 1, prop-2: 2,prop-3: 3}