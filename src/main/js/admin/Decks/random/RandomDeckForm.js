import React, {Component} from 'react'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class RandomDeckForm extends Component {
  constructor(props) {
    super(props)
    this.toggle = this.toggle.bind(this)
  }

  isSelected(color) {
    return this.props.randomColors.indexOf(color) > -1
  }

  toggle(color) {
    const colors = this.props.randomColors
    if (this.isSelected(color)) {
      colors.splice(colors.indexOf(color), 1)
      this.props.setRandomColors([...colors])

    } else {
      this.props.setRandomColors([...colors, color])
    }
  }

  render() {
    return (
      <>
        <p>Choose which colors you want to play:</p>
        <ul>
          <li>
            <input type='checkbox' id='color-white' name='white' checked={this.isSelected('WHITE')} onChange={() => this.toggle('WHITE')}/>
            <label htmlFor='color-white'><img src='/img/symbols/WHITE.png' alt='white'/>White</label>
          </li>
          <li>
            <input type='checkbox' id='color-blue' name='blue' checked={this.isSelected('BLUE')} onChange={() => this.toggle('BLUE')}/>
            <label htmlFor='color-blue'><img src='/img/symbols/BLUE.png' alt='blue'/>Blue</label>
          </li>
          <li>
            <input type='checkbox' id='color-black' name='black' checked={this.isSelected('BLACK')} onChange={() => this.toggle('BLACK')}/>
            <label htmlFor='color-black'><img src='/img/symbols/BLACK.png' alt='black'/>Black</label>
          </li>
          <li>
            <input type='checkbox' id='color-red' name='red' checked={this.isSelected('RED')} onChange={() => this.toggle('RED')}/>
            <label htmlFor='color-red'><img src='/img/symbols/RED.png' alt='red'/>Red</label>
          </li>
          <li>
            <input type='checkbox' id='color-green' name='green' checked={this.isSelected('GREEN')} onChange={() => this.toggle('GREEN')}/>
            <label htmlFor='color-green'><img src='/img/symbols/GREEN.png' alt='green'/>Green</label>
          </li>
        </ul>
      </>
    )
  }
}

const setRandomColors = (colors) => {
  return {
    type: 'DECK_RANDOM_COLORS',
    value: colors
  }
}

const mapStateToProps = state => {
  return {
    randomColors: get(state, 'decks.random.colors', [])
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    setRandomColors: bindActionCreators(setRandomColors, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(RandomDeckForm)
