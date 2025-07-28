import { NavigateOptions, To, useNavigate } from 'react-router'

const useGoTo = () => {
  const navigate = useNavigate()

  const goToBack = () => navigate(-1)

  const goTo = (to: To, options?: NavigateOptions) => () => {
    navigate(to, options);
  }

  return {
    goToBack,
    goTo
  }
}

export default useGoTo
