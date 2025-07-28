import StocktakingDashboard from './stocktaking-dashboard';
import StocktakingProvider from './stocktaking-provider';

const Stocktaking = () => {
  return (
    <StocktakingProvider>
      <StocktakingDashboard />
    </StocktakingProvider>
  );
};

export default Stocktaking;
