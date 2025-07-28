import React from 'react'
import './CardV2.scss'

type Props = {
  header: React.ReactNode,
  children: React.ReactNode,
}

const CardV2 = (props: Props) => {
  const { header, children } = props

  return (
    <main className='card-v2'>
      <section className='card-v2__header'>
        {header}
      </section>
      <section className='card-v2__body'>
        {children}
      </section>
    </main>
  )
}

export default CardV2
