using AntMe.English;
using System;
using System.Collections.Generic;

/// <summary>
/// AntMe! project as part of the module "AGI".
/// </summary>
namespace AntMe.Player.LipkeAnts
{
    [Player(
        ColonyName = "LipkeAnts",
        FirstName = "Felix",
        LastName = "Lipke"
    )]
    
    /// <summary>
    /// Specialized in fighting (with focus on bugs).
    /// But also gathers apples. Ignores sugar.
    /// </summary>
    [Caste(
        Name = "NormalAnt",
        AttackModifier = 2,
        EnergyModifier = 2,
        LoadModifier = -1,
        RangeModifier = -1,
        RotationSpeedModifier = -1,
        SpeedModifier = 0,
        ViewRangeModifier = -1
    )]

    /// <summary>
    /// Same as normal ants, but different behaviour.
    /// Gathers also sugar.
    /// </summary>
    [Caste(
        Name = "SugarAnt",
        AttackModifier = 2,
        EnergyModifier = 2,
        LoadModifier = -1,
        RangeModifier = -1,
        RotationSpeedModifier = -1,
        SpeedModifier = 0,
        ViewRangeModifier = -1
    )]
    public class LipkeAntsClass : BaseAnt
    {
        #region Marker Info Type Members


        private int infoTypeApple = 9;
        private int infoTypeSugar = 8;
        private int infoTypeBug = 7;
        private int infoTypeForeignAnt = 6;

        #endregion

        #region Caste specific Members/Properties

        private string normalAnt = "NormalAnt";
        private string sugarAnt = "SugarAnt";

        private bool IsGameProgressed { get; set; }

        private bool IsSugarAnt
        {
            get { return Caste == sugarAnt; }
        }

        private bool IsNormalAnt
        {
            get { return Caste == normalAnt; }
        }

        private bool? HasSeenForeignAnt { get; set; }

        /// <summary>
        /// When a sugar ant has seen a foreign ant
        /// it should behave like a normal ant.
        /// </summary>
        private bool ShouldSugarAntActLikeNormal
        {
            get { return IsSugarAnt && HasSeenForeignAnt == true; }
        }

        #endregion

        #region Destination Properties

        private bool HasNoDestination
        {
            get { return (Destination == null); }
        }

        private bool IsGoingToAntHill
        {
            get { return (Destination is Anthill); }
        }

        private bool IsGoingToApple
        {
            get { return (Destination is Fruit); }
        }

        private bool IsFollowingBug
        {
            get { return Destination is Bug; }
        }

        private bool IsCarringSugarToAntHill
        {
            get { return (IsGoingToAntHill && IsCarryingSugar); }
        }

        #endregion

        #region Food Carrying Properties
        
        private bool HasNoLoad
        {
            get { return (CurrentLoad == 0); }
        }

        private bool HasLoad
        {
            get { return (CurrentLoad > 0); }
        }

        private bool IsCarryingApple
        {
            get { return (CarryingFruit != null); }
        }

        private bool IsCarryingSugar
        {
            get { return (!IsCarryingApple && HasLoad); }
        }

        #endregion

        #region Health Status Properties

        private bool HasLowRangeLeft
        {
            get { return DistanceToAnthill > (Range - WalkedRange - 50); }
        }

        private bool HasLowEnergy
        {
            get { return CurrentEnergy < MaximumEnergy * 2 / 3; }
        }

        private bool MustRefresh
        {
            get { return HasLowRangeLeft || HasLowEnergy; }
        }

        #endregion

        #region Caste

        /// <summary>
        /// Every time that a new ant is born, its job group must be set.
        /// You can do so with the help of the value returned by
        /// this method.
        /// Read more: "http://wiki.antme.net/en/API1:ChooseCaste"
        /// </summary>
        /// <param name="typeCount">Number of ants for every caste</param>
        /// <returns>Caste-Name for the next ant</returns>
        public override string ChooseCaste(Dictionary<string, int> typeCount)
        {
            int countOfNormalAnts = typeCount[normalAnt];
            int countOfSugarAnts = typeCount[sugarAnt];
            int totalCount = countOfNormalAnts + countOfSugarAnts;
            double sugarAntsShare = countOfSugarAnts / (double)totalCount;
            string caste = string.Empty;

            if(totalCount > 0 && (sugarAntsShare < 0.25))
            {
                caste = sugarAnt;
            }
            else
            {
                caste = normalAnt;
            }
            IsGameProgressed = totalCount > 60; // Maximum is 100

            return caste;
        }

        #endregion

        #region Movement

        /// <summary>
        /// If the ant has no assigned tasks, it waits for new tasks.
        /// This method is called to inform you that it is waiting.
        /// Read more: "http://wiki.antme.net/en/API1:Waiting"
        /// </summary>
        public override void Waiting()
        {  
            if (IsNormalAnt || ShouldSugarAntActLikeNormal)
            {
                // Should not go straigth forward to extend the search areas.
                // Increases the possibility to spot an apple or enemy.
                GoForward(40);
                TurnByDegrees(RandomNumber.Number(-10, 10));
            }
            else if (IsSugarAnt)
            {
                // Should find sugar trace.
                GoForward(150);
                TurnByDegrees(RandomNumber.Number(-15, 15));
            }
        }

        /// <summary>
        /// This method is called when an ant has travelled
        /// one third of its movement range.
        /// Read more: "http://wiki.antme.net/en/API1:GettingTired"
        /// </summary>
        public override void GettingTired()
        {
            // Here could be called "GoToAnthill()"
            // to ensure ants don't die of starvation
            // or low energy (disadvantage in fight).

            // BUT "GettingTired()" gets only called
            // when there is 1/3 of range left. This
            // is very inflexible. It doesn't covers
            // the refresh when the ant has low energy.
            // Also in many situations one thrid might be
            // too late, because the ant hill is too far
            // away (e.g. ant hill is at the very right side).

            // Therefore "MustRefresh" (on tick) is an own mechanism
            // to control when an ant should go back to ant hill.
        }

        /// <summary>
        /// This method is called if an ant dies. It informs you that
        /// the ant has died. The ant cannot undertake any more
        /// actions from that point forward.
        /// Read more: "http://wiki.antme.net/en/API1:HasDied"
        /// </summary>
        /// <param name="kindOfDeath">Kind of Death</param>
        public override void HasDied(KindOfDeath kindOfDeath)
        {
            // As there are no more actions possible,
            // it don't make any sense to use this event
            // with non-static ants.
            // However as an !static! ant it could be used to react
            // on certain circumstances and adjust the spawn castes.
        }

        /// <summary>
        /// This method is called in every simulation round, regardless of
        /// additional conditions. It is ideal for actions that must be
        /// executed but that are not addressed by other methods.
        /// Read more: "http://wiki.antme.net/en/API1:Tick"
        /// </summary>
        public override void Tick()
        {
            // There are most likely no foreign ants.
            if (WalkedRange > 200 && IsGameProgressed && HasSeenForeignAnt == null)
            {
                HasSeenForeignAnt = false;
            }

            // Decision making.
            // React on currenct circumstances.
            if (MustRefresh) // else the ant might die.
            {
                GoToAnthill();
            }
            else if (IsCarringSugarToAntHill)
            {
                MakeTraceToSugar();
            }
            else if (IsCarryingApple)
            {
                // Ensure destination is not lost 
                // and spread if apple needs more carriers.
                HandleApple();
            }
            else if (IsGoingToApple && !NeedsCarrier((Fruit)Destination))
            {
                Stop(); // Don't follow - do something different instead.
            }
        }

        #endregion

        #region Food

        /// <summary>
        /// This method is called as soon as an ant sees an apple
        /// within its 360 degree visual range. The parameter is
        /// the piece of fruit that the ant has spotted.
        /// Read more: "http://wiki.antme.net/en/API1:Spots(Fruit)"
        /// </summary>
        /// <param name="fruit">spotted fruit</param>
        public override void Spots(Fruit fruit)
        {
            if (HasNoLoad && HasNoDestination && NeedsCarrier(fruit))
            {
                GoToDestination(fruit);
                MakeOtherAntsAwareOfApple();
            }
        }

        /// <summary>
        /// This method is called as soon as an ant sees a mound of
        /// sugar in its 360 degree visual range. The parameter is
        /// the mound of sugar that the ant has spotted.
        /// Read more: "http://wiki.antme.net/en/API1:Spots(Sugar)"
        /// </summary>
        /// <param name="sugar">spotted sugar</param>
        public override void Spots(Sugar sugar)
        {
            if (DistanceToAnthill < 600)
            {
                // If we are alone sugar has higher prio
                if (HasSeenForeignAnt != true)
                {
                    int direction = Coordinate.GetDegreesBetween(this, sugar);
                    int distance = Coordinate.GetDistanceBetween(this, sugar);

                    int information = CreateMarkerInformation(direction, infoTypeSugar);
                    MakeMark(information, distance);
                }

                // If ant has load, it does not need more sugar.
                // If it has a destination: ignore the sugar,
                // the destination might be a bug, ant hill, ...
                if (HasNoLoad && HasNoDestination)
                {
                    GoToDestination(sugar);
                }
            }
        }

        /// <summary>
        /// If the ant's destination is a piece of fruit, this method
        /// is called as soon as the ant reaches its destination. 
        /// It means that the ant is now near enough to its 
        /// destination/target to interact with it.
        /// Read more: "http://wiki.antme.net/en/API1:DestinationReached(Fruit)"
        /// </summary>
        /// <param name="fruit">reached fruit</param>
        public override void DestinationReached(Fruit fruit)
        {
            if (NeedsCarrier(fruit)) // Ensure apple still needs carrier.
            {
                TakeFoodToAntHill(fruit);
            }
            if (NeedsCarrier(fruit)) // Still?
            {
                MakeOtherAntsAwareOfApple();
            }
        }

        /// <summary>
        /// If the ant's destination is a mound of sugar, this method
        /// is called as soon as the ant has reached its destination.
        /// It means that the ant is now near enough to its 
        /// destination/target to interact with it.
        /// Read more: "http://wiki.antme.net/en/API1:DestinationReached(Sugar)"
        /// </summary>
        /// <param name="sugar">reached sugar</param>
        public override void DestinationReached(Sugar sugar)
        {
            int direction = Coordinate.GetDegreesBetween(this, sugar);
            int information = CreateMarkerInformation(direction, infoTypeSugar);
            MakeMark(information, 80);
            TakeFoodToAntHill(sugar);
        }

        #endregion

        #region Communication

        /// <summary>
        /// Friendly ants can detect markers left by other ants.
        /// This method is called when an ant smells a friendly 
        /// marker for the first time.
        /// Read more: "http://wiki.antme.net/en/API1:DetectedScentFriend(Marker)"
        /// </summary>
        /// <param name="marker">marker</param>
        public override void DetectedScentFriend(Marker marker)
        {
            // Rehash information.
            var markerInfo = new MarkerInformation(marker.Information);

            // Decision making, based on information.
            
            if (markerInfo.InfoType == infoTypeBug)
            {
                OnDetectedBugMarker(marker);
            }
            else if (markerInfo.InfoType == infoTypeApple)
            {
                OnDetectedAppleMarker(marker, markerInfo.Data);
            }
            else if (markerInfo.InfoType == infoTypeSugar)
            {
                OnDetectedSugarMarker(markerInfo.Data);
            }
            else if (markerInfo.InfoType == infoTypeForeignAnt)
            { // There is a foreign ant!
                HasSeenForeignAnt = true;
            }
        }

        /// <summary>
        /// Just as ants can see various types of food, they can
        /// also visually detect other game elements. This method
        /// is called if the ant sees an ant from the same colony.
        /// Read more: "http://wiki.antme.net/en/API1:SpotsFriend(Ant)"
        /// </summary>
        /// <param name="ant">spotted ant</param>
        public override void SpotsFriend(Ant ant)
        {
            // Could be used for group building or information transfer.
            // But there are no groups in this strategy and for all different
            // information (apple, bug, sugar, foreign ants) markers are 
            // already made and in addition with the possibility to spread
            // advanced information (see "CreateMarkerInformation()")
            // there is no need for further information spreading.
        }

        /// <summary>
        /// Just as ants can see various types of food, they can
        /// also visually detect other game elements. This method
        /// is called if the ant detects an ant from a friendly
        /// colony (an ant on the same team).
        /// Read more: "http://wiki.antme.net/en/API1:SpotsTeammate(Ant)"
        /// </summary>
        /// <param name="ant">spotted ant</param>
        public override void SpotsTeammate(Ant ant)
        {
            // Reason why not used is same as "SpotsFriend()".
        }

        #endregion

        #region Fight

        /// <summary>
        /// Just as ants can see various types of food, they can
        /// also visually detect other game elements. This method
        /// is called if the ant detects an ant from an enemy colony.
        /// Read more: "http://wiki.antme.net/en/API1:SpotsEnemy(Ant)"
        /// </summary>
        /// <param name="ant">spotted ant</param>
        public override void SpotsEnemy(Ant ant)
        {
            // It is sufficient to only spread the info once per ant. 
            if (HasSeenForeignAnt != true)
            {
                // Range of 500 to get many friends informed.
                SpreadInfoForeignAntsExist(500);
            }

            // Apples are besides bugs the main source for gathering points.
            // Therefore only attack ants if not currently carrying an apple.
            if (!IsCarryingApple)
            {
                // One-on-One (Offence is the beste Defense).
                Drop(); // Sugar is less important
                Attack(ant);
            }
        }

        /// <summary>
        /// Just as ants can see various types of food, they can
        /// also visually detect other game elements. This method
        /// is called if the ant sees a bug.
        /// Read more: "http://wiki.antme.net/en/API1:SpotsEnemy(Bug)"
        /// </summary>
        /// <param name="bug">spotted bug</param>
        public override void SpotsEnemy(Bug bug)
        {
            // Radius of 150 is optimal:
            // a) not to small (we need friends to help us)
            // b) not to big (if too big our friend might be too late for the fight)
            MakeOtherAntsAwareOfBug(150);

            // Apples are besides bugs the main source for gathering points.
            // Therefore only attack bugs if not currently carrying an apple.
            if (!IsCarryingApple)
            {
                Drop(); // Sugar is less important
                if (FriendlyAntsInViewrange >= 3) // Not alone!
                {
                    Attack(bug);
                }
                else // Follow as long there aren't enough friendly ants.
                {
                    GoToDestination(bug);
                }
            }
        }

        /// <summary>
        /// Enemy creatures may actively attack the ant. This method
        /// is called if an enemy ant attacks; the ant can then 
        /// decide how to react.
        /// Read more: "http://wiki.antme.net/en/API1:UnderAttack(Ant)"
        /// </summary>
        /// <param name="ant">attacking ant</param>
        public override void UnderAttack(Ant ant)
        {
            // Fight is inescapable.
            Drop();
            Attack(ant);
        }

        /// <summary>
        /// Enemy creatures may actively attack the ant. This method
        /// is called if a bug attacks; the ant can decide how to react.
        /// Read more: "http://wiki.antme.net/en/API1:UnderAttack(Bug)"
        /// </summary>
        /// <param name="bug">attacking bug</param>
        public override void UnderAttack(Bug bug)
        {
            // One ant is not enough for a bug.
            MakeOtherAntsAwareOfBug(150);

            // Fight is inescapable.
            Drop();
            Attack(bug);
        }

        #endregion

        #region Methods

        /// <summary>
        /// Advanced marker information.
        /// Enables ants to provide more information via the marker.
        /// </summary>
        /// <param name="information">Info for other ants e.g. direction.</param>
        /// <param name="infoType">Kind of spotted item (apple, bug or sugar, foreign ant).</param>
        /// <returns>The advanced marker information.</returns>
        private int CreateMarkerInformation(int information, int infoType)
        {
            // Build a string which contains extended information.
            // String must be build in such a way it can
            // be converted to an integer without information loss.
            string _infoType = infoType.ToString();
            string _information = information.ToString().PadLeft(4, '0');
            string advancedInformation = _infoType + _information;

            return Convert.ToInt32(advancedInformation);
        }
        
        private void MakeTraceToSugar()
        {
            if (IsSugarAnt && HasSeenForeignAnt != true)
            {
                int directionToSugar = Direction + 180; // sugar is opposite from ant hill.
                int information = CreateMarkerInformation(directionToSugar, infoTypeSugar);
                int range = DistanceToAnthill > 50 ? 40 : 80;
                MakeMark(information, range);
            }
        }

        private void MakeOtherAntsAwareOfApple()
        {
            // If near to ant hill more carriers are not necessary needed.
            if (DistanceToAnthill > 100)
            {
                // Tell other ants how many carriers are really needed.
                int antsNeeded = Math.Max(0, 8 - FriendlyAntsInViewrange);
                int information = CreateMarkerInformation(antsNeeded, infoTypeApple);
                MakeMark(information, 60);
            }
        }

        private void MakeOtherAntsAwareOfBug(int range)
        {
            int information = CreateMarkerInformation(0, infoTypeBug);
            MakeMark(information, range);
        }

        private void SpreadInfoForeignAntsExist(int range)
        {
            // Spread the information that I have seen an foreign ant.
            int information = CreateMarkerInformation(0, infoTypeForeignAnt);
            MakeMark(information, range);
            HasSeenForeignAnt = true;
        }

        private void TakeFoodToAntHill(Food food)
        {
            Take(food);
            GoToAnthill();
        }

        private void HandleApple()
        {
            GoToAnthill();
            MakeOtherAntsAwareOfApple();
        }

        private void OnDetectedSugarMarker(int sugarDirection)
        {
            if (IsSugarAnt // Normal ants should concentrate on bugs and apples.
                && HasNoDestination 
                && HasNoLoad
                && HasSeenForeignAnt != true 
                && Direction != sugarDirection)
            {
                TurnToDirection(sugarDirection);
                GoForward(150);
            }
        }

        private void OnDetectedAppleMarker(Marker marker, int antsNeeded)
        {
            if (HasNoLoad 
                && HasNoDestination 
                && FriendlyAntsInViewrange < antsNeeded)
            {
                GoToDestination(marker); // Go to middle of the mark.
            }
            else
            {
                Stop(); // Apple does not need more carriers
            }
        }


        private void OnDetectedBugMarker(Marker marker)
        {
            // Bugs have Prio! Therefore the ant should ignore
            // whether it is carrying sugar or not.
            if (FriendlyAntsInViewrange < 8 // else would too many ants go to the bug.
                && (HasNoDestination 
                    || (!IsFollowingBug && !IsCarryingApple) // apples also important.
                    || (IsNormalAnt && IsCarryingSugar)
                    || (ShouldSugarAntActLikeNormal && IsCarryingSugar)))
            {
                Drop(); // Sugar
                GoToDestination(marker);
            }
        }

        #endregion
    }

    #region Helper Class

    /// <summary>
    /// Helper Class for advanced marker information.
    /// </summary>
    public class MarkerInformation
    {
        /// <summary>
        /// Extract information data and type.
        /// </summary>
        /// <param name="information">The advanced information from the marker.</param>
        public MarkerInformation(int information)
        {
            string info = information.ToString();
            this.Data = Convert.ToInt32(info.Substring(1));
            // First char is info type.
            this.InfoType = Convert.ToInt32(info.Substring(0, 1));
        }

        /// <summary>
        /// Apple: ants needed,
        /// Sugar: direction,
        /// Bug: zero (none),
        /// ForeignAnt: zero (none)
        /// </summary>
        public int Data { get; set; }

        /// <summary>
        /// 9: Apple,
        /// 8: Sugar,
        /// 7: Bug,
        /// 6: ForeignAnt
        /// </summary>
        public int InfoType { get; set; }
    }

    #endregion
}