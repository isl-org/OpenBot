const express = require('express')
const {spawn} = require('child_process');
const app = express()
const port = process.env.PORT || 3000;

app.get('/:x/:y', (req, res) => {
let {x} = req.params;
let {y} = req.params;

 var dataToSend;
 // spawn new child process to call the python script
 const python = spawn('python', ['script1.py',x,y]);
 // collect data from script
 python.stdout.on('data', function (data) {
  console.log('Pipe data from python script ...');

  dataToSend = data.toString();
 });
 // in close event we are sure that stream from child process is closed
 python.on('close', (code) => {
 console.log(`child process close all stdio with code ${code}`);
 console.log(dataToSend);
 // send data to browser
 res.send(dataToSend)
 });
 
})

app.listen(port, () => console.log(`Example app listening on port 
${port}!`))