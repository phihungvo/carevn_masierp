import StocktakingProvider from './stocktaking-provider';
import StocktakingUpdateForm from './stocktaking-update-form';

const StocktakingUpdate = () => {
  return (
    <StocktakingProvider>
      <StocktakingUpdateForm />
    </StocktakingProvider>
  );
};

export default StocktakingUpdate;
