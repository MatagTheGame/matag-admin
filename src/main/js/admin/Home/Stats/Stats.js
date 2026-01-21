import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'

export default function Stats() {
  const dispatch = useDispatch()

  const { totalUsers, onlineUsers, totalCards, totalSets } = useSelector(state => ({
    totalUsers: state.stats?.value?.totalUsers || 0,
    onlineUsers: state.stats?.value?.onlineUsers || [],
    totalCards: state.stats?.value?.totalCards || 0,
    totalSets: state.stats?.value?.totalSets || 0
  }))

  useEffect(() => {
    let timer

    const updateStats = () => {
      dispatch({ type: 'STATS_LOADING' })
      ApiClient.get('/stats').then(response => {
        dispatch({ type: 'STATS_LOADED', value: response })
      })
      timer = setTimeout(updateStats, 60000)
    }

    updateStats()

    return () => clearTimeout(timer)
  }, [dispatch])

  return (
    <section id='stats'>
      <ul>
        <li>
          <small><span>TOTAL USERS: </span></small>
          <span>{totalUsers}</span>
        </li>
        <li>
          <small><span>ONLINE USERS: </span></small>
          <span>
            {onlineUsers.length} ({onlineUsers.map(user => (
              <span className="online-user" key={user}>{user}</span>
            ))})
          </span>
        </li>
        <li>
          <small><span>TOTAL CARDS: </span></small>
          <span>{totalCards}</span>
        </li>
        <li>
          <small><span>TOTAL SETS: </span></small>
          <span>{totalSets} (from Magic Origins)</span>
        </li>
      </ul>
    </section>
  )
}
