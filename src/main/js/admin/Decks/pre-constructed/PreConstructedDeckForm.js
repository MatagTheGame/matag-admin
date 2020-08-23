import React, {Component} from 'react'
import {connect} from 'react-redux'

class PreConstructedDeckForm extends Component {
  constructor(props) {
    super(props)
  }

  render() {
    return <div>Pre-Constructed Decks coming soon.</div>
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

export default connect(mapStateToProps, mapDispatchToProps)(PreConstructedDeckForm)