const express = require('express');
const bodyParser = require("body-parser");

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: false
}));

app.use(express.static('public'));

// MONGOOSE

const mongoose = require('mongoose');

// connect to the database
mongoose.connect('mongodb://localhost:27017/test', {
  useUnifiedTopology: true,
  useNewUrlParser: true
});

const itemSchema = new mongoose.Schema({
  desc: String,
  name: String,
  completed: Boolean
});

itemSchema.set('toJSON', {
  virtuals: true
});

// create a virtual paramter that turns the default _id field into id
itemSchema.virtual('id')
  .get(function() {
    return this._id.toHexString();
  });

// Ensure virtual fields are serialised when we turn this into a JSON object
itemSchema.set('toJSON', {
  virtuals: true
});

// create a model for tickets
const Item = mongoose.model('Item', itemSchema);

/*
app.get('/api/list', async (req, res) => {
  try {
    let items = await Item.find();
    res.send({
      items: items
    });
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
});

app.post('/api/list', async (req, res) => {
  const item = new Item({
    name: req.body.name,
    problem: req.body.problem
  });
  try {
    await item.save();
    res.send({
      item: item
    });
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
});

app.put('/api/list/:id', async (req, res) => {
  try {
    let item = await Item.find({
      _id: req.params.id
    });
    item.desc = req.body.desc;
    item.name = req.body.name;
    item.completed = req.body.completed;
    res.sendStatus(200);
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
});

app.delete('/api/list/:id', async (req, res) => {
  try {
    await Item.deleteOne({
      _id: req.params.id
    });
    res.sendStatus(200);
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
});
*/

// NODE

let list = [];
let id = 0;

app.get('/api/list', (req, res) => {
  res.send(list);
});

app.get('/api/list/:id', (req, res) => {
  let id = req.body.id;
  list.find((product, id) => { 
    res.send(list[id]); 
  })
});

app.post('/api/list', (req, res) => {
  id = id + 1;
  let item = {
    id:  id,
    desc: req.body.desc,
    name: req.body.name,
    completed: req.body.completed
  };
  list.push(item);
  res.send(item);
});

app.put('/api/list/:id', (req, res) => {
  let id = parseInt(req.params.id);
  let itemsMap = list.map(item => {
    return item.id;
  });
  let index = itemsMap.indexOf(id);
  if (index === -1) {
    res.status(404)
      .send("Sorry, that item doesn't exist");
    return;
  }
  let item = list[index];
  item.desc = req.body.desc;
  item.name = req.body.name;
  item.completed = req.body.completed;
  res.send(item);
});

app.delete('/api/list/:id', (req, res) => {
  let id = parseInt(req.params.id);
  let removeIndex = list.map(item => {
      return item.id;
    })
    .indexOf(id);
  if (removeIndex === -1) {
    res.status(404)
      .send("Sorry, that item doesn't exist");
    return;
  }
  list.splice(removeIndex, 1);
  res.sendStatus(200);
});


app.listen(3000, () => console.log('Server listening on port 3000!'));
