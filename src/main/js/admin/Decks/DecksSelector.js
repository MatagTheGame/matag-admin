import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import RandomDeckForm from 'admin/Decks/RandomDeckForm'

class DecksSelector extends Component {
  constructor(props) {
    super(props)
  }

  renderRandomColors() {
    return <RandomDeckForm goToGame={this.props.goToGame}  />
  }

  renderPreConstructed() {
    return <div>Pre-Constructed Decks coming soon</div>
  }

  renderCustom() {
    return <div>Custom Decks coming soon</div>
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
          {this.props.deckType === 'random' && this.renderRandomColors()}
          {this.props.deckType === 'pre-constructed' && this.renderPreConstructed()}
          {this.props.deckType === 'custom' && this.renderCustom()}
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
    deckType: get(state, 'decks.type', 'random')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    changeDeckType: bindActionCreators(changeDeckType, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(DecksSelector)