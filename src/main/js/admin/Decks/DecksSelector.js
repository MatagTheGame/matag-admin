import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import RandomColorDeckForm from 'admin/Decks/RandomColorDeckForm'

class DecksSelector extends Component {
  render() {
    return (
      <section>
        <h3>Random Colors</h3>
        <RandomColorDeckForm />
      </section>
    )
  }
}

const changeDeckType = (deckType) => {
  return {
    type: 'DECK_TYPE',
    value: deckType
  }
}

const mapStateToProps = state => {
  return {
    deckType: get(state, 'decks.type', 'randomColors')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    changeDeckType: bindActionCreators(changeDeckType, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(DecksSelector)