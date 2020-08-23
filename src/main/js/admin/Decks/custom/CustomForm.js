import React, {Component} from 'react'
import {connect} from 'react-redux'

class CustomDeckForm extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    return <div>Custom Decks coming soon for registered users only.</div>
  }
}


const mapStateToProps = () => {
  return {

  }
}

const mapDispatchToProps = () => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(CustomDeckForm)