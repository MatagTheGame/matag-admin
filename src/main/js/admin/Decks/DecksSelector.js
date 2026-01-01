import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import AbstractDeckForm from 'admin/Decks/AbstractDeckForm'
import DecksSelectorUtils from 'admin/Decks/DecksSelectorUtils'
import PreConstructedDeckForm from 'admin/Decks/pre-constructed/PreConstructedDeckForm'
import CustomDeckForm from 'admin/Decks/custom/CustomForm'
import RandomDeckForm from 'admin/Decks/random/RandomDeckForm'

class DecksSelector extends Component {
  constructor(props) {
    super(props)
  }

  getTabHeaderClassName(headerName) {
    let className = 'tab-header'

    if (this.props.deckType === headerName) {
      className += ' selected'
    }

    return className
  }

  render() {
    return (
      <section className='tab-container'>
        <div className='tab-list'>
          <div className={this.getTabHeaderClassName('random')} onClick={() => this.props.changeDeckType('random')}><h3>Random</h3></div>
          <div className={this.getTabHeaderClassName('pre-constructed')} onClick={() => this.props.changeDeckType('pre-constructed')}><h3>PreConstructed</h3></div>
          <div className={this.getTabHeaderClassName('custom')} onClick={() => this.props.changeDeckType('custom')}><h3>Custom</h3></div>
        </div>
        <div className='tab-content'>
          {this.props.deckType === 'random' && <AbstractDeckForm goToGame={this.props.goToGame}><RandomDeckForm /></AbstractDeckForm>}
          {this.props.deckType === 'pre-constructed' && <AbstractDeckForm goToGame={this.props.goToGame}><PreConstructedDeckForm /></AbstractDeckForm>}
          {this.props.deckType === 'custom' && <AbstractDeckForm goToGame={this.props.goToGame}><CustomDeckForm /></AbstractDeckForm>}
        </div>
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
    deckType: DecksSelectorUtils.getDeckType(state)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    changeDeckType: bindActionCreators(changeDeckType, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(DecksSelector)
